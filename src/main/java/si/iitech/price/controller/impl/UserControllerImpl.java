package si.iitech.price.controller.impl;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;

import si.iitech.lib.controller.UserController;
import si.iitech.price.entity.impl.EtPriceUser;


@Controller
@RequestMapping("/users")
public class UserControllerImpl extends UserController<EtPriceUser> {

	public UserControllerImpl(BCryptPasswordEncoder bCryptPasswordEncoder) {
		super(bCryptPasswordEncoder);
	}

}
