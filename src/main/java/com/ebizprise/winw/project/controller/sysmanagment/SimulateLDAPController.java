package com.ebizprise.winw.project.controller.sysmanagment;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

import org.springframework.core.io.ClassPathResource;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.ModelAndView;

import com.ebizprise.winw.project.base.controller.BaseController;

/**
 * 模擬合庫使用 Web service 方式取的 AD 帳號及群組方式
 */
@RestController
@RequestMapping("/ldap")
public class SimulateLDAPController extends BaseController {

	@RequestMapping(value = "/getAllUser", method = RequestMethod.GET, produces = "application/xml;charset=UTF-8")
	public String getAllUser() {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new ClassPathResource("users_sample.xml").getInputStream(), "UTF-8"));
			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	@RequestMapping(value = "/getAllGroup", method = RequestMethod.GET, produces = "application/xml;charset=UTF-8")
	public String getAllGroup() {
		StringBuilder sb = new StringBuilder();

		try {
			BufferedReader br = new BufferedReader(
					new InputStreamReader(new ClassPathResource("groups_sample.xml").getInputStream(), "UTF-8"));
			// read line by line
			String line;
			while ((line = br.readLine()) != null) {
				sb.append(line).append("\n");
			}
		} catch (IOException e) {
			e.printStackTrace();
		}

		return sb.toString();
	}

	@Override
	public ModelAndView initPage() {
		return null;
	}
}
