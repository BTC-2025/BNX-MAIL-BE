package com.btctech.mailapp.strategy;

import com.btctech.mailapp.dto.RegisterRequest;
import com.btctech.mailapp.entity.AccountType;
import com.btctech.mailapp.entity.BusinessProfile;
import com.btctech.mailapp.entity.Organization;
import com.btctech.mailapp.entity.User;
import com.btctech.mailapp.repository.BusinessProfileRepository;
import com.btctech.mailapp.repository.UserRepository;
import com.btctech.mailapp.service.OrganizationService;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class BusinessRegistrationStrategy implements RegistrationStrategy {

    private final UserRepository userRepository;
    private final OrganizationService organizationService;
    private final BusinessProfileRepository businessProfileRepository;
    private final PasswordEncoder passwordEncoder;

    @Override
    @Transactional
    public User register(RegisterRequest request) {
        // 1. Create Organization
        Organization org = organizationService.createOrganization(
                request.getBusinessName(),
                request.getDomain()
        );

        // 2. Create User (Owner)
        User user = new User();
        user.setUsername(request.getUsername());
        user.setPassword(passwordEncoder.encode(request.getPassword()));
        user.setFirstName(request.getOwnerFirstName());
        user.setLastName(request.getOwnerLastName());
        user.setAccountType(AccountType.BUSINESS);
        user.setOrganization(org);
        user.setRole("ORG_ADMIN");
        user.setApproved(true);
        user.setActive(true);

        user = userRepository.save(user);

        // 3. Create Business Profile
        BusinessProfile profile = new BusinessProfile();
        profile.setUser(user);
        profile.setBusinessName(request.getBusinessName());
        profile.setBusinessType(request.getBusinessType());
        profile.setRegistrationNumber(request.getRegistrationNumber());

        businessProfileRepository.save(profile);

        return user;
    }

    @Override
    public String getMode() {
        return "BUSINESS";
    }
}
