package com.apap.tutorial4.controller;

import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.NoSuchElementException;

import com.apap.tutorial4.model.*;
import com.apap.tutorial4.service.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.ui.Model;

/**
 * @author DELL
 * DealerController
 */
@Controller
public class DealerController {
	@Autowired
	private DealerService dealerService;
	
	@Autowired
	private CarService CarService;
	
	@RequestMapping("/")
	private String home() {
		return "home";
	}
	
	@RequestMapping(value = "/dealer/add", method = RequestMethod.GET)
	private String add(Model model) {
		model.addAttribute("dealer", new DealerModel());
		return "addDealer";
	}
	
	@RequestMapping(value = "/dealer/add", method = RequestMethod.POST)
	private String addDealerSubmit(@ModelAttribute DealerModel dealer) {
		dealerService.addDealer(dealer);
		return "add";
}
	@RequestMapping(value = "/dealer/update/{dealerId}", method = RequestMethod.GET)
	private String updateDealer(@PathVariable(value = "dealerId") Long dealerId, Model model) {
		model.addAttribute("dealer", new DealerModel());
		return "updatedealer";
}
	@RequestMapping(value = "/dealer/update/{dealerId}", method = RequestMethod.POST)
	private String updateDealer(@PathVariable(value = "dealerId") Long dealerId,@ModelAttribute DealerModel dealer,Model model) {
		dealerService.updateDealer(dealerId, dealer.getAlamat(), dealer.getNoTelp());
		return "updatedealerberhasil";
}
	
	@RequestMapping(value = "/dealer/remove/{dealerId}", method = RequestMethod.GET)
	private String deleteDealer(@PathVariable(value = "dealerId") Long dealerId, Model model) {
		DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
		dealerService.deleteDealer(dealer);
		return "remove";
}
	@RequestMapping(value = "/dealer/viewall", method = RequestMethod.GET)
	private String viewDealer(Model model) {
		List<DealerModel> banyakdealer = dealerService.getAllDealer();
		for(int x= 0;x<banyakdealer.size();x++) {
			List<CarModel> listCar  = banyakdealer.get(x).getListCar();
			Collections.sort(listCar, new SortCar());
		}
		model.addAttribute("listdealer",banyakdealer);
		return "viewall";
}
	@RequestMapping(value = "/dealer/view", method = RequestMethod.GET)
	private String viewDealerSubmit(@RequestParam("dealerId") Long dealerId, Model model) {
		try {
			DealerModel dealer = dealerService.getDealerDetailById(dealerId).get();
			model.addAttribute("dealer", dealer);
			model.addAttribute("dealerId", dealerId);
			List<CarModel> listCar  = dealerService.getDealerDetailById(dealerId).get().getListCar();
			Collections.sort(listCar, new SortCar());
			model.addAttribute("listCar", listCar);
			return "view-dealer";
		}
		catch(NoSuchElementException e) {
			return "notfound";
		}
}
	class SortCar implements Comparator<CarModel> 
	{ 
	    public int compare(CarModel a, CarModel b) 
	    { 
	    	Integer priceA = Integer.parseInt(a.getPrice());
	    	Integer priceB = Integer.parseInt(b.getPrice());
	    	
	        return priceA - priceB; 
	    } 
	} 
}
