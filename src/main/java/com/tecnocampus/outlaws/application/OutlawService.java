package com.tecnocampus.outlaws.application;

import com.tecnocampus.outlaws.application.dto.OutlawDTO;
import com.tecnocampus.outlaws.domain.Outlaw;
import com.tecnocampus.outlaws.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class OutlawService {

    private final UserRepository userRepository;

    public OutlawService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public OutlawDTO createOutlaw(OutlawDTO outlawDTO) {
        Outlaw outlaw = new Outlaw(outlawDTO);
        userRepository.addUser(outlaw);
        return outlaw.toDTO();
    }

    public List<OutlawDTO> getAllOutlaws() {
        return userRepository.getAllOutlaws().stream().map(Outlaw::toDTO).toList();
    }

    public OutlawDTO getOutlawById(String id) {
        return userRepository.getOutlawById(id).toDTO();
    }

    public OutlawDTO updateOutlaw(String id, OutlawDTO outlawDTO) {
        Outlaw outlaw = userRepository.getOutlawById(id);
        outlaw.update(outlawDTO);
        userRepository.updateOutlaw(outlaw);
        return outlaw.toDTO();
    }

    public OutlawDTO updateBounty(String id, Map<String, String> updates) {
        Outlaw outlaw = userRepository.getOutlawById(id);
        updates.forEach((key, value) -> {
            switch (key) {
                case "bounty" -> outlaw.setBounty(Integer.valueOf(updates.get("bounty")));
            }
        });
        userRepository.updateOutlaw(outlaw);
        return outlaw.toDTO();
    }


    public void deleteOutlaw(String id) {
        userRepository.removeUser(id);
    }

    public OutlawDTO getMostWantedOutlaw() {
        return userRepository.getMostWantedOutlaw().toDTO();
    }
}
