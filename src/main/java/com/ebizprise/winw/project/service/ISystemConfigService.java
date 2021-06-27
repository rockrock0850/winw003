package com.ebizprise.winw.project.service;

import java.util.List;

import com.ebizprise.winw.project.vo.SysParameterVO;

public interface ISystemConfigService {

	public void syncParameterList();

	public List<SysParameterVO> selectAllParameters();

	public List<SysParameterVO> selectParametersByKey(String paramKey);

	public void updateParameter(SysParameterVO vo);

	/**
     * 取得系統參數檔中的檔案副檔名之定義值
	 * 
	 * @return
	 */
    public SysParameterVO getFileExtension ();

    /**
     * 取得系統參數檔中的檔案大小之定義值
     * 
     * @return
     */
    public SysParameterVO getFileSize ();

    /**
     * 取得衝擊分析分數資訊
     * 
     * @return
     */
    public SysParameterVO getFraction ();

    /**
     * 取得寄信資訊
     * 
     * @return
     */
    public SysParameterVO getMail ();

}
