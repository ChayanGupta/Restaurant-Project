package com.infiniti.main.repo;

import org.springframework.data.repository.CrudRepository;

import com.infiniti.main.modal.Message;

public interface MessageRepository extends CrudRepository<Message, String> {

}
