package org.zerock.guestbook.entity;

import lombok.Getter;

import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import javax.persistence.Column;
import javax.persistence.EntityListeners;
import javax.persistence.MappedSuperclass;

import java.time.LocalDateTime;

@MappedSuperclass//테이블로 생성되지 않게 막는다.
@EntityListeners(value = {AuditingEntityListener.class})
@Getter
abstract class BaseEntity {

	@CreatedDate
	@Column(name = "regdate", updatable = false)
	private LocalDateTime regDate;

	@LastModifiedDate
	@Column(name = "moddate")
	private LocalDateTime modDate;

}
