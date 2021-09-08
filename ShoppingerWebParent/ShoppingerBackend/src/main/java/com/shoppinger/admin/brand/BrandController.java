package com.shoppinger.admin.brand;

import com.shoppinger.admin.FileUploadUtil;
import com.shoppinger.admin.category.CategoryService;
import com.shoppinger.admin.paging.PagingAndSortingHelper;
import com.shoppinger.admin.paging.PagingAndSortingParam;
import com.shoppinger.common.exception.BrandNotFoundException;
import com.shoppinger.common.entity.Brand;
import com.shoppinger.common.entity.Category;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;

import java.io.IOException;
import java.util.List;

@Controller
public class BrandController {

    @Autowired
    private BrandService brandService;
    @Autowired
    private CategoryService categoryService;


    @GetMapping("/brands")
    public String listFirstPage() {
        return "redirect:/brands/page/1?sortField=name&sortDir=asc";
    }

    @GetMapping("/brands/page/{pageNum}")
    public String listByPage(
            @PagingAndSortingParam(listName = "brandList", moduleURL = "/brands")PagingAndSortingHelper helper,
            @PathVariable("pageNum") int pageNum){

        brandService.listBrandsByPage(pageNum,helper);

        return "brands/brands";
    }

    @GetMapping("/brands/new")
    public String newBrand(Model model){
        List<Category> categoryList = categoryService.listCategoriesUsedInForm();

        model.addAttribute("categoryList",categoryList);
        model.addAttribute("brand", new Brand());
        model.addAttribute("pageTitle", "Create New Brand");

        return "brands/brand_form";
    }

    @PostMapping("/brands/save")
    public String saveBrand(Brand brand, @RequestParam("fileImage")MultipartFile multipartFile,
                            RedirectAttributes redirectAttributes) throws IOException {
        if (!multipartFile.isEmpty()) {
            String fileName = StringUtils.cleanPath(multipartFile.getOriginalFilename());
            brand.setLogo(fileName);

            Brand savedBrand = brandService.save(brand);
            String uploadDir = "brand-logos/" + savedBrand.getId();

            FileUploadUtil.cleanDir(uploadDir);
            FileUploadUtil.saveFile(uploadDir,fileName,multipartFile);
        }
        else {
            brandService.save(brand);
        }
        redirectAttributes.addFlashAttribute("message", "The brand has been saved succesfuly.");
        return "redirect:/brands";
    }

    @GetMapping("/brands/edit/{id}")
    public String editBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes)
    {
        try {
         Brand brand = brandService.getBrandById(id);
         List<Category> categoryList = categoryService.listCategoriesUsedInForm();

         model.addAttribute("brand", brand);
         model.addAttribute("categoryList",categoryList);
         model.addAttribute("pageTitle", "Edit Brand (ID: "+ id + ")");

         return "brands/brand_form";
        } catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
            return "redirect:/brands";
        }
    }

    @GetMapping("/brands/delete/{id}")
    public String deleteBrand(@PathVariable(name = "id") Integer id, Model model, RedirectAttributes redirectAttributes) {
        try {
            brandService.delete(id);
            String brandDir = "brand-logos/"+ id;
            FileUploadUtil.removeDir(brandDir);

            redirectAttributes.addFlashAttribute("message", "The brand deleted succesfully ID: "+ id);
        }catch (BrandNotFoundException e) {
            redirectAttributes.addFlashAttribute("message", e.getMessage());
        }
        return "redirect:/brands";
    }
}
