package com.example.repository.specification;

import com.example.common.entity.Category;
import lombok.AllArgsConstructor;
import org.springframework.data.jpa.domain.Specification;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import java.util.ArrayList;
import java.util.List;

@AllArgsConstructor
public class CategorySpecification implements Specification<Category> {
	private String keyword;

	@Override
	public Predicate toPredicate(Root<Category> root, CriteriaQuery<?> cq, CriteriaBuilder cb) {
		List<Predicate> predicates = new ArrayList<>();
		if (keyword != null){
			predicates.add(cb.like(cb.lower(root.get("name").as(String.class)), keyword.concat("%")));
			predicates.add(cb.like(cb.lower(root.get("id").as(String.class)), keyword));
		}
		Predicate search = null;
		if (!predicates.isEmpty()){
			search = cb.or(predicates.toArray(new Predicate[] {}));
		}
		return search;
	}
}
