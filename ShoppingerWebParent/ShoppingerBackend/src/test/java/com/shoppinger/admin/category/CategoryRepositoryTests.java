package com.shoppinger.admin.category;

import com.shoppinger.common.entity.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Sort;
import org.springframework.test.annotation.Rollback;

import java.util.List;
import java.util.Set;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest(showSql = false)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@Rollback(value = false)
public class CategoryRepositoryTests {

    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    public void testCreateRootCategory(){
        Category category = new Category("Electronics");
        Category savedCategory = categoryRepository.save(category);

        assertThat(savedCategory.getId()).isGreaterThan(0);

    }
    @Test
    public void testCreateSubCategory(){
        Category parent =  new Category(7);
        Category subCat = new Category("Android", parent);

        Category savedCategory = categoryRepository.save(subCat);
        assertThat(savedCategory.getId()).isGreaterThan(0);
    }

    @Test
    public void testGetCategory(){
        Category category = categoryRepository.findById(2).get();
        System.out.println(category.getName());

        Set<Category> children = category.getChildren();

        for (Category subCategory : children) {
            System.out.println(subCategory.getName());
        }
        assertThat(children.size()).isGreaterThan(0);
    }
    @Test
    public void testPrintHierarchicalCategories(){
        Iterable<Category> categories = categoryRepository.findAll();

        for (Category category : categories) {
            if(category.getParent() == null) {
                System.out.println(category.getName());

                Set<Category> children = category.getChildren();
                for (Category subCategory : children){
                    System.out.println("--" + subCategory.getName());
                    printChildren(subCategory, 1);
                }
            }
        }

    }
    private void printChildren(Category parent,int subLevel){
        int newSublevel = subLevel + 1;
        Set<Category> children = parent.getChildren();

        for (Category subCategory : children) {
            for (int i = 0; i < newSublevel; i++){
                System.out.print("--");
            }
            System.out.println(subCategory.getName());
            printChildren(subCategory,newSublevel);

        }
    }

    @Test
    public void testListRootCategories(){
        List<Category> rootCategories = categoryRepository.findRootCategories(Sort.by("name").ascending());
        rootCategories.forEach(cat -> System.out.println(cat.getName()));
    }

    @Test
    public void testFindByName() {
        String name  = "Computers";
        Category category = categoryRepository.findByName(name);

        assertThat(category).isNotNull();
        assertThat(category.getName()).isEqualTo(name);
    }

    @Test
    public void testFindByAlias() {
        String alias  = "Computers";
        Category category = categoryRepository.findByAlias(alias);

        assertThat(category).isNotNull();
        assertThat(category.getAlias()).isEqualTo(alias);
    }

}
