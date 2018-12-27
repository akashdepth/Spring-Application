package com.bs.social;

/**
 * Created by personal on 10/29/18.
 */

import com.bs.social.models.ContentSection;
import org.springframework.data.repository.CrudRepository;

import java.util.List;


public interface ContentSectionRepository extends CrudRepository<ContentSection, Integer> {
    List<ContentSection> findAllByOrderByTimestampDesc();
    ContentSection findById(Long id);
}