package com.tecnocampus.outlaws.application;

import com.tecnocampus.outlaws.application.dto.OutlawDTO;
import com.tecnocampus.outlaws.application.dto.SheriffDTO;
import com.tecnocampus.outlaws.domain.Outlaw;
import com.tecnocampus.outlaws.domain.Sheriff;
import com.tecnocampus.outlaws.persistence.UserRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;

@Service
public class SheriffService {

    private final UserRepository userRepository;

    public SheriffService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public SheriffDTO createSheriff(SheriffDTO sheriffDTO) {
        Sheriff sheriff = new Sheriff(sheriffDTO);
        userRepository.addUser(sheriff);
        return sheriff.toDTO();
    }

    public List<SheriffDTO> getAllSheriffs() {
        return userRepository.getAllSheriffs().stream().map(Sheriff::toDTO).toList();
    }

    public SheriffDTO getSheriffById(String id) {
        return userRepository.getSheriffById(id).toDTO();
    }

    public SheriffDTO updateSheriff(String id, SheriffDTO sheriffToUpdate) {
        Sheriff sheriff = userRepository.getSheriffById(id);
        sheriff.update(sheriffToUpdate);
        userRepository.updateSheriff(sheriff);
        return sheriff.toDTO();
    }


    public SheriffDTO updateSheriffField(String id, Map<String, Object> updates) {
        Sheriff sheriff = userRepository.getSheriffById(id);
        updates.forEach((key, value) -> {
            switch (key) {
                case "name" -> sheriff.setName((String) value);
                case "salary" -> sheriff.setSalary((Integer) value);
            }
        });
        userRepository.updateSheriff(sheriff);

        return sheriff.toDTO();
    }


    public OutlawDTO captureOutlaw(String sheriffId, String outlawId) {
        Sheriff sheriff = userRepository.getSheriffById(sheriffId);
        Outlaw outlaw = userRepository.getOutlawById(outlawId);
        sheriff.captureOutlaw(outlaw);
        userRepository.updateSheriff(sheriff);
        userRepository.updateOutlaw(outlaw);
        return outlaw.toDTO();
    }


    public OutlawDTO eliminateOutlaw(String sheriffId, String outlawId) {
        Sheriff sheriff = userRepository.getSheriffById(sheriffId);
        Outlaw outlaw = userRepository.getOutlawById(outlawId);
        sheriff.eliminateOutlaw(outlaw);
        userRepository.updateSheriff(sheriff);
        userRepository.updateOutlaw(outlaw);
        return outlaw.toDTO();
    }

    public void deleteSheriff(String id) {
        userRepository.removeUser(id);
    }
}
