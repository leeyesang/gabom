package icia.project.gabom.service;

import java.security.Principal;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.google.gson.Gson;

import icia.project.gabom.dao.IsnstimelineDao;
import icia.project.gabom.dto.Snsposts;

@Service
public class SnsTimeLineService {
	
	@Autowired
	private IsnstimelineDao stDao;

	public String snsTimeLine(Snsposts snsposts, Principal principal) {
		String json = null;
		System.out.println("timeline 전체글 보여줘");
		
		List<Snsposts> snsTimeLine = stDao.getsnsTimeLine(snsposts);
		
		json = new Gson().toJson(snsTimeLine);
		System.out.println("json="+json);
		
		return json;
	}

	public String mytimeline(Snsposts snsposts, Principal principal) {
		String json = null;
		System.out.println("내 타임라인 글");
		
		String sns_posts_writer =principal.getName();
		System.out.println("내아이디"+sns_posts_writer);
		
		List<Snsposts> mytimeline = stDao.getmytimeline(snsposts,sns_posts_writer);
		
		json = new Gson().toJson(mytimeline);
		System.out.println("json="+json);
		
		return json;
	
	}

	public String friendtimeline(Snsposts snsposts, Principal principal) {
		String json = null;
		System.out.println("친구 타임라인 글");
		
		String friend_my_id =principal.getName();
		System.out.println(friend_my_id);
		List<Snsposts> friendtimeline = stDao.getfriendtimeline(snsposts, friend_my_id);
		
		json = new Gson().toJson(friendtimeline);
		System.out.println("json="+json);
		
		return json;
	}
	

}
