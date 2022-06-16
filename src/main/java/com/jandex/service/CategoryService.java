package com.jandex.service;

import com.jandex.entity.Category;
import com.jandex.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public Category getByExternalId(UUID id) {
        return categoryRepository.findCategoryByExternalId(id);
    }

}
