package com.jandex.service;

import com.jandex.entity.Category;
import com.jandex.repository.CategoryRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

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

    public Category update(Category category)
    {
        return categoryRepository.saveAndFlush(category);
    }

    public Optional<Category> getCategoryByUUID(UUID id) {
        return categoryRepository.findById(id);
    }

    public List<Category> getChildren(Category parent) {
        return categoryRepository.findAllByParentId(parent.getId());
    }

    public void delete(Category category) {
        categoryRepository.deleteById(category.getId());
    }
}
