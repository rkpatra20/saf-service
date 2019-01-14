package com.monamitech.task.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;

import com.monamitech.task.model.SAFModel;
import com.monamitech.task.model.SAFRetryFailure;
import com.monamitech.task.model.SAFStatus;
import com.monamitech.task.util.JsonUtils;

@Repository
@Transactional
public class SAFModelDao {

	private final String INSERT_SAF_MODEL = "INSERT INTO TBL_SAF(SERVICE_NAME,REQUEST,FROM_SYSTEM,TO_SYSTEM,STATUS,FAILURES,RETRY_COUNT) VALUES(:SN,:REQ,:FS,:TS,:STATUS,:FAILURES,:RETRY_COUNT)";

	private final String SELECT_SAF_MODEL_Q1 = "SELECT * FROM TBL_SAF WHERE SERVICE_NAME= :SN AND RETRY_COUNT < :RC AND STATUS= :STATUS";

	private final String SELECT_SAF_MODEL_Q2 = "SELECT * FROM TBL_SAF WHERE SAF_ID=:SAF_ID";

	private final String SELECT_SAF_MODEL_Q3 = "SELECT FAILURES FROM TBL_SAF WHERE SAF_ID=:SAF_ID";

	private final String UPDATE_ON_ERROR = "UPDATE TBL_SAF SET FAILURES=:FAILURES, RETRY_COUNT=:RETRY_COUNT WHERE SAF_ID=:SAF_ID";

	private final String UPDATE_ON_SUCCESS = "UPDATE TBL_SAF SET RESPONSE=:RESPONSE,STATUS=:STATUS, RETRY_COUNT=:RETRY_COUNT WHERE SAF_ID=:SAF_ID AND STATUS=:PENDING";

	private final String SELECT_SAF_MODEL_Q4 = "SELECT * FROM TBL_SAF";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public SAFModel insertSAFModel(SAFModel model) {
		Map<String, Object> inputs = new HashMap<>();

		inputs.put("SN", model.getServiceName());
		inputs.put("REQ", model.getRequest());
		inputs.put("FS", model.getFromSystem());
		inputs.put("TS", model.getToSystem());
        inputs.put("RETRY_COUNT", 0);
		inputs.put("STATUS", SAFStatus.PENDING.name());
		inputs.put("FAILURES", JsonUtils.toString(model.getRetryFailures()));
        
		KeyHolder kh = new GeneratedKeyHolder();
		MapSqlParameterSource source = new MapSqlParameterSource(inputs);
		namedParameterJdbcTemplate.update(INSERT_SAF_MODEL, source, kh);
		model.setSafId(kh.getKey().intValue());
		return model;
	}

	public List<SAFModel> importSAFModel(SAFModel model) {
		//Map<String, Object> inputs = new HashMap<>();
		MapSqlParameterSource inputs = new MapSqlParameterSource();
		inputs.addValue("SN", model.getServiceName());
    	inputs.addValue("RC", model.getRetryCount());
		inputs.addValue("STATUS", SAFStatus.valueOf(model.getStatus()).name());
		List<SAFModel> models = new ArrayList<>();
		try {
			models = namedParameterJdbcTemplate.query(SELECT_SAF_MODEL_Q1, inputs, new SAFModelRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
			e.printStackTrace();
		}
		return models;
	}

	public int updateSAFModelOnError(Integer safId, String failureText, Integer retryCount) {
		List<SAFRetryFailure> models = findSAFRetryFailureById(safId);
		SAFRetryFailure safRetryFailure = new SAFRetryFailure();
		safRetryFailure.setFailureResponse(failureText);
		safRetryFailure.setCurrentRetryCount(retryCount + 1);
		safRetryFailure.setRetryAt(new Date());
		models.add(safRetryFailure);
		String safRetryFailureJson = JsonUtils.toString(models);

		Map<String, Object> inputs = new HashMap<>();
		inputs.put("FAILURES", safRetryFailureJson);
		inputs.put("SAF_ID", safId);
		inputs.put("RETRY_COUNT", retryCount);
		MapSqlParameterSource source = new MapSqlParameterSource(inputs);
		return namedParameterJdbcTemplate.update(UPDATE_ON_ERROR, source);

	}

	public int updateSAFModelOnSuccess(Integer safId, String response, Integer retryCount) {
		Map<String, Object> inputs = new HashMap<>();
		inputs.put("RESPONSE", response);
		inputs.put("SAF_ID", safId);
		inputs.put("STATUS", SAFStatus.SUCCESS.name());
		inputs.put("RETRY_COUNT", retryCount);
		MapSqlParameterSource source = new MapSqlParameterSource(inputs);
		return namedParameterJdbcTemplate.update(UPDATE_ON_SUCCESS, source);
	}

	public List<SAFModel> selectAll(String status) {
		Map<String, Object> paramMap = new HashMap<>();
		StringBuffer sbQuery = new StringBuffer(SELECT_SAF_MODEL_Q4);

		if (validSafStatus(status)) {
			sbQuery.append(" WHERE STATUS=:STATUS");
			paramMap.put("STATUS", status);
		}
		List<SAFModel> list=new ArrayList<>();
		try {
			list= namedParameterJdbcTemplate.query(sbQuery.toString(), paramMap, new SAFModelRowMapper());
		} catch (EmptyResultDataAccessException e) {
			// TODO: handle exception
		}
		return list;
	}

	private boolean validSafStatus(String status) {
		// TODO Auto-generated method stub
		try {
			SAFStatus.valueOf(status);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
			return false;
		}
	}

	public SAFModel findById(Integer safId) {
		Map<String, Object> inputs = new HashMap<>();
		inputs.put("SAF_ID", safId);
		MapSqlParameterSource source = new MapSqlParameterSource(inputs);
		SAFModel model = new SAFModel();
		try {
			model = namedParameterJdbcTemplate.queryForObject(SELECT_SAF_MODEL_Q2, source, new SAFModelRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}

	public List<SAFRetryFailure> findSAFRetryFailureById(Integer safId) {
		List<SAFRetryFailure> list = new ArrayList<>();
		try {
			Map<String, Object> inputs = new HashMap<>();
			inputs.put("SAF_ID", safId);
			MapSqlParameterSource source = new MapSqlParameterSource(inputs);
			String result = namedParameterJdbcTemplate.queryForObject(SELECT_SAF_MODEL_Q3, source, String.class);
			SAFRetryFailure[] safRetryFailures = JsonUtils.toObject(result, SAFRetryFailure[].class);
			list.addAll(Arrays.asList(safRetryFailures));
		} catch (EmptyResultDataAccessException e) {
			// e.printStackTrace();
		}
		return list;
	}
}

class SAFModelRowMapper implements RowMapper<SAFModel> {

	@Override
	public SAFModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		SAFModel model=new SAFModel();
		model.setStatus(rs.getString("STATUS"));;
		model.setServiceName(rs.getString("SERVICE_NAME"));;
		model.setSafId(rs.getInt("SAF_ID"));;
		model.setRetryCount(rs.getInt("RETRY_COUNT"));;
		model.setFromSystem(rs.getString("FROM_SYSTEM"));;
		model.setToSystem(rs.getString("FROM_SYSTEM"));
		model.setInsertedDt(rs.getDate("INSERTED_AT"));;
		System.out.println("model.hash");
		return model;
	}

}
