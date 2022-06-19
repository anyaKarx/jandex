package com.jandex.service;

import com.jandex.entity.Category;
import com.jandex.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class CategoryService {
    private final CategoryRepository categoryRepository;

    public Category save(Category category) {
        return categoryRepository.save(category);
    }

    public List<Category> findAll() {
        return categoryRepository.findAll();
    }

    public Optional<Category> getCategoryByUUID(UUID id) {
        return categoryRepository.findById(id);
    }

    @Transactional
    public void delete(Category category){ categoryRepository.delete(category);}

}
