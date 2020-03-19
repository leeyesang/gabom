package icia.project.gabom.service;

import java.security.Principal;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.servlet.ModelAndView;

import com.google.gson.Gson;

import icia.project.gabom.dao.ITripplanDao;
import icia.project.gabom.dto.Food;
import icia.project.gabom.dto.Member;
import icia.project.gabom.dto.Sns_friend;
import icia.project.gabom.dto.Trip_member;
import icia.project.gabom.dto.Trip_plan;

@Service
public class TripService {
   private ModelAndView mav;
   
   @Autowired
   private ITripplanDao tpDao;
   
   
   //여행 플랜 1단계
   public String savetripplan(Trip_plan tp, Principal ppl) throws ParseException {
      String json =null;
      System.out.println("저장하러 오니?");
      String DATE_PATTERN = "yyyy-MM-dd";//날짜 패턴 선언
      
      String trip_id = ppl.getName();
      String trip_title = tp.getTrip_title();
      String trip_start_date = tp.getTrip_start_date();
      String trip_end_date = tp.getTrip_end_date();
      String trip_area = tp.getTrip_area();
      
      Trip_plan tpl = new Trip_plan();
      
      tpl.setTrip_area(trip_area).setTrip_id(trip_id).setTrip_title(trip_title);
      tpl.setTrip_start_date(trip_start_date).setTrip_end_date(trip_end_date);
      
      int trip_number = tpDao.savetripplan(tpl);
      
      System.out.println("currval값="+tpl.getTrip_number());
      
      int trip_number2=tpl.getTrip_number();
      System.out.println("여행번호:"+trip_number2);
      
       SimpleDateFormat sdf = new SimpleDateFormat(DATE_PATTERN); //날짜 패턴 형식으로 변환
        
       Date startDate = sdf.parse(trip_start_date);//시작날짜
        
       Date endDate = sdf.parse(trip_end_date);//종료날짜
        
       ArrayList<String> day = new ArrayList<String>();//날짜를 리스트에 담음

       Date currentDate = startDate;//날짜 current

        while (currentDate.compareTo(endDate) <= 0) {
           day.add(sdf.format(currentDate));
               Calendar c = Calendar.getInstance();
               c.setTime(currentDate);
               c.add(Calendar.DAY_OF_MONTH, 1);
               currentDate = c.getTime();
           }
           int index=1;
           for (String date : day) {
               System.out.println(date);
            boolean d = tpDao.savetripdate(trip_number2,date,index);
            index++;
           }
           
           System.out.println("여행 날짜 전부"+day);

      json = new Gson().toJson(tpl);
      
      return json;
   }


   //내 여행 목록 
   public ModelAndView myplan(Principal ppl,Trip_member tm) {
      String json = null;
      String json2 = null;
      String json3 = null;
      String json4 = null;
      String json5 = null;
      
      mav = new ModelAndView();
      String view = null;
      
      String trip_id = ppl.getName();
      
      
      List<Trip_plan> myplanlist = tpDao.getmyplan(trip_id);//내 여행목록
      
      List<Member> memberinfo = tpDao.getmemberinfo(trip_id);//회원정보
      List<Sns_friend> friendlist = tpDao.getfriendlist(trip_id);//회원의 친구목록
      
      List<Trip_member> reqlist =tpDao.requestmember(trip_id);//친구한테 요청한 것
  	  List<Trip_member> myreqlist =tpDao.requestme(trip_id);//나한테 온 것.
  	  
      view="Trip/myplan";
      
      json = new Gson().toJson(myplanlist);
      json2 = new Gson().toJson(memberinfo);
      json3 = new Gson().toJson(friendlist);
      json4 = new Gson().toJson(reqlist);
      json5 = new Gson().toJson(myreqlist);
      
      mav.addObject("myplanlist", json); // key,value
      mav.addObject("memberinfo", json2); // key,value
      mav.addObject("friendlist", json3); // key,value
      mav.addObject("reqlist", json4); // key,value
      mav.addObject("myreqlist", json5); // key,value
      
      
      
      
      
      
      
      
      //System.out.println("reqlist"+json4);
      //System.out.println("myreqlist"+json5);
      
      
      mav.setViewName(view); //view에 url로 이동
      return mav;
   
   }

   //친구 초대 
   public String togetherplan(Trip_member tm, Principal ppl) {
	String json = null;
	String json2 = null;
	
	String share_id=tm.getShare_id();
	int trip_number =tm.getTrip_number();
	String trip_id = ppl.getName();
	//System.out.println(trip_id);
	
	tm.setShare_id(share_id).setTrip_number(trip_number);
	
	boolean addfriend = tpDao.togetherplan(tm);
	
	Map<String,List<Trip_member>> list = new HashMap<String, List<Trip_member>>();
	
	List<Trip_member> reqlist =tpDao.requestmember(trip_id);//친구한테 요청한 것
	//System.out.println(reqlist);
	
	List<Trip_member> myreqlist =tpDao.requestme(trip_id);//나한테 온 것.
	//System.out.println(myreqlist);
	
	list.put("reqlist", reqlist);
	list.put("myreqlist", myreqlist);
	
	json = new Gson().toJson(list);

	
	return json;
}
   //초대 승인
	public String accepttrip(Trip_member tm, Principal ppl) {
		String json = null;
		
		String share_id=ppl.getName();
		int trip_number =tm.getTrip_number();
		System.out.println(share_id + trip_number);
		
		
		boolean acc= tpDao.accepttrip(share_id,trip_number);
		
		json = new Gson().toJson(acc);
		
		return json;
}

	//초대 거절
	public String rejecttrip(Trip_member tm, Principal ppl) {
		String json = null;
		
		String share_id=ppl.getName();
		int trip_number =tm.getTrip_number();
		System.out.println(share_id + trip_number);
		
		
		boolean acc= tpDao.rejecttrip(share_id,trip_number);
		
		json = new Gson().toJson(acc);
		
		return json;

	}

	//상세 플랜 페이지 
	public ModelAndView detailplan(int trip_number,Principal ppl) {
		//System.out.println("상세 여행페이지로");
		
		mav = new ModelAndView();
		String json = null;
	    String view = null;
	    
	    
	    List<Trip_plan> detrip = tpDao.detailplan(trip_number);
	    
	    view="Trip/detailplan";
		
	    json = new Gson().toJson(detrip);
	    mav.addObject("detrip", json);
	    System.out.println(json);
	    
	    mav.setViewName(view);
		return mav;
	}
   
   

}