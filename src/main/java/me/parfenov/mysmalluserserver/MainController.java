package me.parfenov.mysmalluserserver;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class MainController {

	private final Logger log = LoggerFactory.getLogger(this.getClass());

	@Autowired
	private UserRepository userRepository;

	@GetMapping("/")
	public Iterable<User> getUsers(
	        @RequestParam(defaultValue="0") Integer page,
            @RequestParam(value = "pagelength", defaultValue="${getusers.pagelength.default}") Integer length,
            @Value("${getusers.pagelength.max}") Integer maxLength) {

        return userRepository.findAll(null, PageRequest.of(page, Math.min(length, maxLength))).getContent();

	}

	@GetMapping("/{id}")
	ResponseEntity<User> getUser(@PathVariable Integer id) {
		return new ResponseEntity<User>(userRepository.findById(id).get(), HttpStatus.OK);
	}

	@PutMapping("/")
	ResponseEntity<User> createUser(@RequestBody @Valid User user) throws MyException {

	    if (user.getId() != null) {
			throw new MyException("A new entry was not added because the id field is not null.");
		}

		HttpHeaders responseHeader = new HttpHeaders();

	    return new ResponseEntity<User>(userRepository.save(user), HttpStatus.OK);

	}

	@PutMapping("/{id}")
    ResponseEntity<User> updateUser(@RequestBody @Valid User user, @PathVariable Integer id) throws MyException {

        if (user.getId() != null && user.getId() != id) {
            throw new MyException("The record was not updated because the id value in the request address does not match the id value in the outgoing JSON.");
        }

        return new ResponseEntity<User>(userRepository.findById(id)
                .map(e -> {
                    e.setName(user.getName());
					e.setAge(user.getAge());
                    e.setEmail(user.getEmail());
					e.setHasCar(user.isHasCar());
					e.setMoneyAmount(user.getMoneyAmount());
                    return userRepository.save(e);
                }).get(), HttpStatus.OK);
    }

	@DeleteMapping("/{id}")
	void deleteUser(@PathVariable Integer id) {
		userRepository.deleteById(id);
	}

    @ResponseStatus(HttpStatus.BAD_REQUEST)
    @ExceptionHandler(MyException.class)
    @ResponseBody ErrorInfo
    handleBadRequest(HttpServletRequest req, Exception ex) {
        return new ErrorInfo(req.getRequestURL().toString(), ex);
    }

}
