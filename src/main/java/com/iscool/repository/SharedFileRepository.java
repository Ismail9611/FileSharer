package com.iscool.repository;


import com.iscool.domain.SharedFile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.sql.Date;
import java.util.List;

@Repository
public interface SharedFileRepository extends CrudRepository<SharedFile, Long>, JpaRepository<SharedFile, Long>{
    SharedFile findByUrlContaining(String url);
    List<SharedFile> findAllByActiveToBefore(Date date);
}
