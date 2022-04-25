package com.example.services.servicesImplement;
import com.example.common.jwt.CustomUserDetail;
import com.example.common.model.AutoIncrement;
import com.example.common.model.ConfirmKey;
import com.example.common.model.User;
import com.example.common.request.CheckEmailRequest;
import com.example.common.request.LoginRequest;
import com.example.common.request.UserRequest;
import com.example.common.response.CommonResponse;
import com.example.common.response.UserResponse;
import com.example.repository.mongo.ConfirmKeyRepository;
import com.example.repository.mongo.UserRepository;
import com.example.services.UserServices;
import com.google.common.hash.Hashing;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.MailException;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;
import java.nio.charset.StandardCharsets;
import java.util.Date;
import java.util.List;
import java.util.Optional;

/**
 * @author Tran Minh Truyen
 */

@Service
public class UserServicesImplement implements UserDetailsService, UserServices {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ConfirmKeyRepository confirmKeyRepository;

    @Autowired
    private JavaMailSender emailSender;

    @Override
    public UserResponse createUser(UserRequest userRequest) throws Exception {
        if (userRequest != null && !accountIsExists(userRequest.getAccount()) && !emailIsExists(userRequest.getEmail()))  {
            List<User> last  = new AutoIncrement(userRepository).getLastOfCollection();
            User newUser = new User();
            newUser.setAccount(userRequest.getAccount());
            newUser.setPassword(Hashing.sha512().hashString(userRequest.getPassword(), StandardCharsets.UTF_8).toString());
            newUser.setFirstName(userRequest.getFirstName());
            newUser.setLastName(userRequest.getLastName());
            newUser.setBirthDay(userRequest.getBirthDay());
            if (last != null)
                newUser.setId(last.get(0).getId()+1);
            else newUser.setId(1);
            newUser.setAddress(userRequest.getAddress());
            newUser.setDistrict(userRequest.getDistrict());
            newUser.setCity(userRequest.getCity());
            newUser.setPostCode(userRequest.getPostCode());
            newUser.setEmail(userRequest.getEmail());
            newUser.setRole(userRequest.getRole());
            newUser.setImage(userRequest.getImage());
            newUser.setActive(true);
            User result = userRepository.save(newUser);
            if (result != null)
                return getUserAfterUpdateOrCreate(result);
            else throw new Exception("Error while created user account");
        }
        else throw new Exception("User account is exists");
    }

    @Override
    public CommonResponse getAllUser(int page, int size) throws Exception {
        List result = userRepository.findAll();
        if (result != null){
            return new CommonResponse().getCommonResponse(page, size, result);
        } else {
            throw new Exception("Not found");
        }
    }

    @Override
    public CommonResponse getUserByKeyWord(int page, int size, String keyword) throws Exception {
        CommonResponse commonResponse = new CommonResponse();
        List result = userRepository.findUserByFirstNameContainingOrLastNameContaining(keyword, keyword);
        if (result != null){
            return new CommonResponse().getCommonResponse(page, size, result);
        }
        else return getAllUser(page, size);
    }

    @Override
    public User login(LoginRequest loginRequest) throws Exception {
        User result = userRepository.findUsersByAccountEqualsAndPasswordEquals(loginRequest.getAccount(),
                Hashing.sha512().hashString(loginRequest.getPassword(), StandardCharsets.UTF_8).toString());
        if (result != null && result.isActive()){
            return result;
        } else {
            if (result != null && !result.isActive()){
                throw new Exception("User is disable");
            } else {
                throw new Exception("Not found user");
            }
        }
    }

    @Override
    public UserResponse updateUser(int id, UserRequest request) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if(user.isPresent()){
            User update = user.get();
            if (request.getFirstName() != null)
                update.setFirstName(request.getFirstName());
            if (request.getPassword() != null)
                update.setPassword(Hashing.sha512().hashString(request.getPassword(), StandardCharsets.UTF_8).toString());
            if (request.getLastName() != null)
                update.setLastName(request.getLastName());
            if (request.getAddress() != null)
                update.setAddress(request.getAddress());
            if (request.getDistrict() != null)
                update.setDistrict(request.getDistrict());
            if (request.getCity() != null)
                update.setCity(request.getCity());
            if (request.getPostCode() != null)
                update.setPostCode(request.getPostCode());
            if (request.getBirthDay() != null)
                update.setBirthDay(request.getBirthDay());
            if (request.getCitizenID() != null)
                update.setCitizenId(request.getCitizenID());
            if (request.getEmail() != null)
                update.setEmail(request.getEmail());
            if (request.getImage() != null)
                update.setImage(request.getImage());
            update.setActive(request.isActive());
            User result = userRepository.save(update);
            if (result != null)
                return getUserAfterUpdateOrCreate(result);
            else throw new Exception("Error while update user");
        }
        else throw new Exception("Not found user");
    }

    @Override
    public boolean deleteUser(int id) throws Exception {
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            userRepository.deleteById(id);
            return true;
        }
        else throw new Exception("Not found user");
    }



    @Override
    public boolean accountIsExists(String account) {
        User checkAccount = userRepository.findUserByAccount(account);
        if (checkAccount != null)
            return true;
        else return false;
    }

    @Override
    public boolean emailIsExists(String email) {
        User checkEmail = userRepository.findUserByEmailEqualsIgnoreCase(email);
        if (checkEmail != null)
            return true;
        else return false;
    }

    @Override
    public UserDetails loadUserByUsername(String account) throws UsernameNotFoundException {
        User user = userRepository.findUserByAccount(account);
        if (user != null){
            return new CustomUserDetail(user);
        }
        else throw new UsernameNotFoundException("User not found");
    }

    @Override
    public UserDetails loadUserById(int id) {
        Optional<User> result = userRepository.findById(id);
        if (result.isPresent()){
            User user = result.get();
            return new CustomUserDetail(user);
        }
        else throw new UsernameNotFoundException("User not found with id: " + id);
    }

    public UserResponse getUserAfterUpdateOrCreate(User user){
        UserResponse response = new UserResponse();
        response.setAddress(user.getAddress());
        response.setBirthDay(user.getBirthDay());
        response.setLastName(user.getLastName());
        response.setFirstName(user.getFirstName());
        response.setCitizenID(user.getCitizenId());
        response.setActive(user.isActive());
        response.setEmail(user.getEmail());
        response.setRole(user.getRole());
        response.setImage(user.getImage());
        return response;
    }

    @Override
    public UserResponse getProfileUser(int id) throws Exception {
        UserResponse userResponse = new UserResponse();
        Optional<User> user = userRepository.findById(id);
        if (user.isPresent()){
            userResponse.setFirstName(user.get().getFirstName());
            userResponse.setLastName(user.get().getLastName());
            userResponse.setBirthDay(user.get().getBirthDay());
            userResponse.setAddress(user.get().getAddress());
            userResponse.setCitizenID(user.get().getCitizenId());
            userResponse.setEmail(user.get().getEmail());
            userResponse.setImage(user.get().getImage());
            userResponse.setActive(user.get().isActive());
            return userResponse;
        }
        else
            throw new Exception("Not found user");
    }

    @Override
    public User resetPassword(String email) throws Exception {
        User user = userRepository.findUserByEmailEqualsIgnoreCase(email);
        if (user != null) {
            String newPassword = RandomStringUtils.randomAlphanumeric(10).toUpperCase();
            user.setPassword(Hashing.sha512().hashString(newPassword, StandardCharsets.UTF_8).toString());
            User after = userRepository.save(user);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("GG-App Reset Password");
            mailMessage.setText("This is your new password: " + newPassword + "\n" + "Please change your password");
            emailSender.send(mailMessage);
            return after;
        }
        else throw new Exception("Not found user");
    }

    @Override
    public void sendEmailConfirmKey(String email, String confirmKey) throws Exception {
        try {
            ConfirmKey newConfirmKey = new ConfirmKey();
            newConfirmKey.setEmail(email);
            newConfirmKey.setKey(confirmKey);
            Date now = new Date();
            Date expireTime = new Date(now.getTime() + 300000);
            newConfirmKey.setExpire(expireTime);
            ConfirmKey isKeyExists = confirmKeyRepository.findByEmailEquals(email);
            if (isKeyExists != null){
                confirmKeyRepository.deleteByEmail(email);
            }
            confirmKeyRepository.save(newConfirmKey);

            SimpleMailMessage mailMessage = new SimpleMailMessage();
            mailMessage.setTo(email);
            mailMessage.setSubject("GG-App Confirm Key");
            mailMessage.setText("This is your confirm key: " + confirmKey + "\n" + "Key will expired in 5 minutes");
            emailSender.send(mailMessage);
        } catch (MailException e) {
            confirmKeyRepository.deleteByEmail(email);
            throw new Exception(e.getMessage());
        }
    }

    @Override
    public boolean checkConfirmKey(String email, String key) {
        ConfirmKey isKeyExists = confirmKeyRepository.findByEmailEquals(email);
        Date now = new Date();
        if (isKeyExists != null && isKeyExists.getKey().equals(key) && now.before(isKeyExists.getExpire())) {
            confirmKeyRepository.deleteByEmail(email);
            return true;
        }
        else return false;
    }
}
