package com.ebizprise.winw.project.repository;

import java.util.List;
import org.springframework.data.jpa.repository.JpaRepository;

import com.ebizprise.winw.project.entity.SysParameterEntity;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.QueryHints;
import org.springframework.stereotype.Repository;

import javax.persistence.QueryHint;

@Repository("sysParameterRepository")
public interface ISysParameterRepository extends JpaRepository<SysParameterEntity, Long> {
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<SysParameterEntity> findAllByOrderByIdAsc();

	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public SysParameterEntity findTop1ByParamId(int paramId);

	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public SysParameterEntity findByParamKey(String paramKey);

	@Query("FROM SysParameterEntity spl WHERE spl.paramKey LIKE %?1%")
	@QueryHints({@QueryHint(name = "org.hibernate.cacheable", value = "true")})
	public List<SysParameterEntity> findByParamKeyLike(String paramKey);
}
