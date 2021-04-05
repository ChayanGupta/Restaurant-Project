package com.infiniti.main.controller;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import com.infiniti.main.modal.Admin;
import com.infiniti.main.modal.Message;
import com.infiniti.main.modal.TableBookings;
import com.infiniti.main.repo.AdminRepo;
import com.infiniti.main.repo.BookingRepo;
import com.infiniti.main.repo.MessageRepository;


@Controller
public class MyController {
	@Autowired MessageRepository repo;
	@Autowired BookingRepo bookingrepo;
	@Autowired AdminRepo adminrepo;
	@Autowired private BCryptPasswordEncoder passwordEncoder;
	
	//main page
	@GetMapping("/index")
	public String home()
	{
		return "index";
	}
	
	//login page
	@GetMapping("/login")
	public String login()
	{
		return "login";
	}
	
	//sign up page
	@GetMapping("/signup")
	public String signup()
	{
		return "signup";
	}
	
	@GetMapping("/adminsignup")
	public String adminSignup()
	{
		return "adminsignup";
	}
	
	
	//sign up complete page
	@PostMapping("/registration")
	public String registration(@ModelAttribute Admin admin, Model model)
	{		
		Admin user = adminrepo.getUserByUserName(admin.getName());
		if(user!=null)
		{	
			model.addAttribute("message", "Username already exists");
			return "adminsignup";
		}
		else if(admin.getSecret().compareTo("some random string here")==0)
		{
			admin.setRole("ROLE_ADMIN");
			admin.setEnabled(true);
			admin.setPass(passwordEncoder.encode(admin.getPass()));
			adminrepo.save(admin);
			model.addAttribute("message", "Registration Successfull!!");
			return "adminsignup";
		}
		else
		{
			model.addAttribute("message", "Please Provide Correct Secret code");
			return "adminsignup";
		}
		
	}
	
	//admin page
	@GetMapping("/admin")
	public String adminlogin(Model model)
	{
		Date date = new Date();
		List<TableBookings> itr =(ArrayList<TableBookings>) bookingrepo.findByDateGreaterThan(date);
		model.addAttribute("data", itr);
		return "admin";
	}
	
	//message page
	@PostMapping("/contact")
	public String receiveMessage(@ModelAttribute Message msg ,BindingResult result,RedirectAttributes redirectAttributes)
	{
		repo.save(msg);
		redirectAttributes.addFlashAttribute("message", "Failed to receive..Please try again");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-danger");
	    if (result.hasErrors()) {
	        return "redirect:/index";
	    }
	    redirectAttributes.addFlashAttribute("message", "Successfully received your message");
	    redirectAttributes.addFlashAttribute("alertClass", "alert-success");
	    return "redirect:/index";
	}
	
	//table booking page
	@PostMapping("/booking")
	public String booking(@RequestParam("date") String date,
						  @RequestParam("name") String name,
						  @RequestParam("email") String email,
						  @RequestParam("phone") String phone,
						  @RequestParam("time") String time,
						  @RequestParam("people") String people,
						  @RequestParam("message") String message,
						  @RequestParam("status") String status,Model model) throws ParseException
	{
		SimpleDateFormat sdf=new SimpleDateFormat("dd/MM/yyyy");
		Date d=sdf.parse(date);
		TableBookings bookings= new TableBookings();
		bookings.setDate(d);
		bookings.setEmail(email);
		bookings.setMessage(message);
		bookings.setName(name);
		bookings.setPeople(Integer.parseInt(people));
		bookings.setPhone(phone);
		bookings.setTime(time);
		bookings.setStatus(status);
		bookingrepo.save(bookings);
		model.addAttribute("message","Your table reservation was placed, but is not yet accepted.You’ll receive an update on Mail.");
		model.addAttribute("alertClass", "alert-success");
		return "index";
	}
	
	//table booking updated page
	@PostMapping("/updated")
	public String confirmBooking(@RequestParam("id") int id,
								 @RequestParam("status") String status, Model model)
	{
		Optional<TableBookings> optional = bookingrepo.findById(id);
		TableBookings booking=optional.get();
		booking.setStatus(status);
		bookingrepo.save(booking);
		model.addAttribute("message", "Booking Confirmed Successfully!!");
		model.addAttribute("alertClass", "alert-success");
		Date date = new Date();
		List<TableBookings> itr =(ArrayList<TableBookings>) bookingrepo.findByDateGreaterThan(date);
		model.addAttribute("data", itr);
		return "admin";
	}
}
