package vn.demo.jobhunter.domain.response;

import java.time.Instant;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import vn.demo.jobhunter.util.constant.GenderEnum;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class ResUserDTO {
	private long id;
	private String name;
	private String email;
	private GenderEnum gender;
	private String address;
	private int age;
	private Instant createAt;
	private Instant updateAt;

	private CompanyUser company;

	@Getter
	@Setter
	@AllArgsConstructor
	@NoArgsConstructor
	public static class CompanyUser {
		public long id;
		public String name;
	}

}
