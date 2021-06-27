package com.ebizprise.winw.project.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.ebizprise.winw.project.entity.FormVerifyLogEntity;

@Repository("formVerifyLogRepository")
public interface IFormVerifyLogRepository extends JpaRepository<FormVerifyLogEntity, Long> {

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelAndVerifyTypeAndVerifyResultNotNull(String formId, String verifyLevel, String verifyType);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyResultNotNullOrderByUpdatedAtDesc (String formId);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelOrderByUpdatedAtDesc (String formId, String verifyLevel);

    public int countByFormId (String formId);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelAndVerifyResultOrderByUpdatedAtDesc (String formId, String verifyLevel, String verifyResult);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyResultOrderByUpdatedAtDesc (String formId, String verifyResult);

    public FormVerifyLogEntity findByFormIdAndVerifyLevelAndCompleteTimeIsNull (String formId, String verifyLevel);
    
    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelAndVerifyResult (String formId, String verifyLevel, String verifyResult);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelOrderByUpdatedAtAsc (String formId, String verifyLevel);
    
    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtAsc (String formId, String jumpLevel, String verifyType);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelAndVerifyTypeOrderByUpdatedAtDesc (String formId, String jumpLevel, String verifyType);

    public List<FormVerifyLogEntity> findByFormIdAndVerifyLevel (String formId, String verifyLevel);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyTypeOrderByUpdatedAtDesc (String formId, String verifyType);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyResultInOrderByUpdatedAtDesc(String formId, List<String> verifyResultList);

    public List<FormVerifyLogEntity> findByFormIdAndVerifyResultAndVerifyType (String formId, String verifyResult, String verifyType);

    public List<FormVerifyLogEntity> findByFormIdAndVerifyResultAndVerifyTypeOrderByUpdatedAtDesc (String formId, String verifyResult, String verifyType);

    public int countByFormIdAndVerifyLevelAndCompleteTimeIsNull (String formId, String verifyLevel);
    
    public int countByFormIdAndVerifyLevelAndCompleteTimeIsNotNull (String formId, String verifyLevel);

    public int countTop1ByFormIdAndVerifyLevelAndCompleteTimeIsNotNullOrderByIdDesc (String formId, String verifyLevel);

    public int countByFormIdAndVerifyLevelAndVerifyTypeAndCompleteTimeIsNull (String formId, String verifyLevel, String verifyType);

    public int countTop1ByFormIdAndVerifyLevelAndVerifyTypeAndCompleteTimeIsNotNullOrderByIdDesc (String formId, String verifyLevel, String verifyType);

    public FormVerifyLogEntity findTop1ByFormIdAndVerifyLevelAndParallelAndCompleteTimeIsNull (String formId, String verifyLevel, String subGroup);

    public List<FormVerifyLogEntity> findByFormIdAndVerifyLevelAndParallel (String formId, String verifyLevel, String subGroup);

    public FormVerifyLogEntity findByFormIdAndVerifyResult (String formId, String name);

    /**
     * 更新當前流程的狀態(當前流程狀態 = CompleteTime IS NULL)
     * 
     * @param formId
     * @param userId
     * @param verifyResult
     * @return
     */
    @Modifying
    @Query("UPDATE FormVerifyLogEntity SET completeTime = :current, userId = :userId, verifyResult = :verifyResult, verifyComment = :verifyComment, updatedBy = :userId WHERE formId = :formId AND completeTime IS NULL")
    public int updateByCompleteTimeIsNull(@Param("current")Date current,@Param("formId")String formId,@Param("userId")String userId,@Param("verifyResult")String verifyResult,@Param("verifyComment")String verifyComment);


}
