package icia.project.gabom;

import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.google.gson.Gson;

import icia.project.gabom.dao.IServiceDao;
import icia.project.gabom.dto.Qnaboard;



@RestController
public class RestServiceCenter {
	@Autowired
	private IServiceDao sDao;
	
	//qna 글작성
	@PostMapping(value = "/qnawrite", produces = "text/plain;charset=UTF-8")
	public String qnawrite(@RequestParam("title") String title, @RequestParam("content")String content
			, Principal principal) {
		System.out.println("레스트컨트룰러 qna작성모달");
		System.out.println("qna작성 제목 : " + title);
		System.out.println("qna작성 내용 : " + content);
		System.out.println("qna작성자 : " + principal.getName());
		boolean result = sDao.getqnawrite(title, content, principal.getName());
		System.out.println("글작성 성공?" + result);

		List<Qnaboard> nlist = sDao.getQnaList(); // 전체qna 정보 출력
		System.out.println("qnalist=" + nlist);
		SimpleDateFormat format1= new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
		for(Qnaboard qna : nlist) {
			String wDate=format1.format(qna.getQna_date());
			qna.setResultDate(wDate);
		}

		String json = new Gson().toJson(nlist);
		System.out.println(json);

		return json;
	}
}
