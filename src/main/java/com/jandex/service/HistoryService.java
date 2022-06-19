package com.jandex.service;


import com.jandex.entity.History;
import com.jandex.entity.Offer;
import com.jandex.repository.HistoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class HistoryService {
    private final HistoryRepository historyRepository;

    public History save(History history) {
        return historyRepository.save(history);
    }

    public void deleteAllById(Offer parent) {
        historyRepository.deleteAllByParent(parent);
    }

}
