package icia.project.gabom.dto;



import java.util.Date;

import org.apache.ibatis.type.Alias;

import lombok.Data;
import lombok.experimental.Accessors;

@Alias("snsDmDto")
@Data
@Accessors(chain = true)
public class SnsDmDto {
	int dmNumber;
	String sendMember;
	String getMember;
	String contents;
	Date dmDate;
	String memberPic;
	String dmDateRsult;
	String resultDate;
	int checkNum;
}
