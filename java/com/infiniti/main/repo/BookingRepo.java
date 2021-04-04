package com.infiniti.main.repo;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import com.infiniti.main.modal.TableBookings;

public interface BookingRepo extends CrudRepository<TableBookings,Integer> {
	@Query("select b from TableBookings b where b.date>=?1")
	public List<TableBookings> findByDateGreaterThan(Date date);
}
