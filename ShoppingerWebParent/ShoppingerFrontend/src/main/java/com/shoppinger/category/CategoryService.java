package com.shoppinger.category;

import com.shoppinger.common.entity.Category;
import com.shoppinger.common.exception.CategoryNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@Service
public class CategoryService {
    @Autowired
    private CategoryRepository categoryRepository;

    public List<Category> listNoChildrenCategories(){
        List<Category> noChildrenCategoryList = new ArrayList<>();
        List<Category> enabledCategoryList = categoryRepository.findAllEnabledCategories();
        enabledCategoryList.forEach(category -> {
            Set<Category> children = category.getChildren();
            if (children == null || children.size() == 0) {
                noChildrenCategoryList.add(category);
            }
        });

        return noChildrenCategoryList;
    }
    public Category getCategory(String alias) throws CategoryNotFoundException {
        Category category = categoryRepository.findByAliasEnabledCategories(alias);
        if(category == null){
            throw new CategoryNotFoundException("Category not found with alias: "+alias);
        }
        return category;
    }

    public List<Category> getCategoryParents(Category child){
        List<Category> parentList = new ArrayList<>();
        Category parent = child.getParent();

        while (parent != null) {
            parentList.add(0,parent);
            parent = parent.getParent();
        }
        parentList.add(child);

        return parentList;
    }

}
