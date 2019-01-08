package com.monamitech.task.dao;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
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
public class SAFModelDao {

	private final String INSERT_SAF_MODEL = "INSERT INTO TBL_SAF(SERVICE_NAME,REQUEST,FROM_SYSTEM,TO_SYSTEM,INSERTED_AT,STATUS,FAILURES) VALUES(:SN,:REQ,:FS,:TS,:INSERTED_AT,:STATUS,:FAILURES)";

	private final String SELECT_SAF_MODEL_Q1 = "SELECT * FROM TBL_SAF WHERE SERVICE_NAME=:SN AND RETRY_COUNT=:RC AND STATUS=:STATUS";

	private final String SELECT_SAF_MODEL_Q2 = "SELECT * FROM TBL_SAF WHERE SAF_ID=:SAF_ID";
	
	private final String SELECT_SAF_MODEL_Q3 = "SELECT FAILURES FROM TBL_SAF WHERE SAF_ID=:SAF_ID";
	
	private final String UPDATE_ON_ERROR="UPDATE TBL_SAF SET FAILURES=:FAILURES, RETRY_COUNT=RETRY_COUNT+1 WHERE SAF_ID=:SAF_ID";
	
	private final String UPDATE_ON_SUCCESS="UPDATE TBL_SAF SET RESPONSE=:RESPONSE,STATUS=:STATUS RETRY_COUNT=RETRY_COUNT+1 WHERE SAF_ID=:SAF_ID";

	@Autowired
	private NamedParameterJdbcTemplate namedParameterJdbcTemplate;

	public SAFModel insertSAFModel(SAFModel model) {
		Map<String, Object> inputs = new HashMap<>();

		inputs.put("SN", model.getServiceName());
		inputs.put("REQ", model.getRequest());
		inputs.put("FS", model.getFromSystem());
		inputs.put("TS", model.getToSystem());
		
		inputs.put("INSERTED_DT", new Date());
		inputs.put("STATUS", SAFStatus.ERROR.name());
        inputs.put("FAILURES", model.getRetryFailures());
        
		KeyHolder kh = new GeneratedKeyHolder();
		MapSqlParameterSource source = new MapSqlParameterSource(inputs);
		namedParameterJdbcTemplate.update(INSERT_SAF_MODEL, source, kh);
		model.setSafId(kh.getKey().intValue());
		return model;
	}

	public List<SAFModel> importSAFModel(SAFModel model) {
		Map<String, Object> inputs = new HashMap<>();

		inputs.put("SN", model.getServiceName());
		inputs.put("RC", model.getRetryCount());
		inputs.put("STATUS", SAFStatus.valueOf(model.getStatus()));

		MapSqlParameterSource source = new MapSqlParameterSource(inputs);

		List<SAFModel> models = new ArrayList<>();
		try {
			models = namedParameterJdbcTemplate.query(SELECT_SAF_MODEL_Q1, source,new SAFModelRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return models;
	}
	
	public int updateSAFModelOnError(Integer safId,String failureText, Integer retryCount)
	{
		List<SAFRetryFailure> models=findSAFRetryFailureById(safId);
		SAFRetryFailure safRetryFailure=new SAFRetryFailure();
		safRetryFailure.setFailureResponse(failureText);
		safRetryFailure.setCurrentRetryCount(retryCount+1);
		safRetryFailure.setRetryAt(new Date());
		models.add(safRetryFailure);
		String safRetryFailureJson=JsonUtils.toString(models);
		
		Map<String,Object> inputs=new HashMap<>();
		inputs.put("FAILURES", safRetryFailureJson);
		inputs.put("SAF_ID", safId);
		MapSqlParameterSource source=new MapSqlParameterSource(inputs);
		return namedParameterJdbcTemplate.update(UPDATE_ON_ERROR, source);
		
	}
	public int updateSAFModelOnSuccess(Integer safId,String response,Integer retryCount)
	{
		Map<String,Object> inputs=new HashMap<>();
		inputs.put("RESPONSE", response);
		inputs.put("SAF_ID", safId);
		inputs.put("STATUS", SAFStatus.SUCCESS.name());
		MapSqlParameterSource source=new MapSqlParameterSource(inputs);
		return namedParameterJdbcTemplate.update(UPDATE_ON_SUCCESS, source);
	}
	
	public SAFModel findById(Integer safId)
	{
		Map<String,Object> inputs=new HashMap<>();
		inputs.put("SAF_ID", safId);
		MapSqlParameterSource source=new MapSqlParameterSource(inputs);
		SAFModel model=new SAFModel();
		try {
			model=namedParameterJdbcTemplate.queryForObject(SELECT_SAF_MODEL_Q2, source, new SAFModelRowMapper());
		} catch (Exception e) {
			// TODO: handle exception
		}
		return model;
	}
	
	public List<SAFRetryFailure> findSAFRetryFailureById(Integer safId)
	{
		Map<String,Object> inputs=new HashMap<>();
		inputs.put("SAF_ID", safId);
		MapSqlParameterSource source=new MapSqlParameterSource(inputs);
		String result=namedParameterJdbcTemplate.queryForObject(SELECT_SAF_MODEL_Q3, source, String.class);
		SAFRetryFailure[] safRetryFailures=JsonUtils.toObject(result, SAFRetryFailure[].class);
		List<SAFRetryFailure> list= new ArrayList<>();
		list.addAll(Arrays.asList(safRetryFailures));
		return list;
	}
}

class SAFModelRowMapper implements RowMapper<SAFModel> {

	@Override
	public SAFModel mapRow(ResultSet rs, int rowNum) throws SQLException {
		// TODO Auto-generated method stub
		// extract all but not the failures
		return null;
	}

}

