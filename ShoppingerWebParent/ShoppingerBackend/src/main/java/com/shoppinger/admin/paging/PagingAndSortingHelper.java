package com.shoppinger.admin.paging;


import com.shoppinger.common.entity.Brand;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.web.method.support.ModelAndViewContainer;

import java.util.List;

public class PagingAndSortingHelper {
    private ModelAndViewContainer model;
    private String listName;
    private String sortField;
    private String sortDir;
    private String keyword;

    public String getSortField() {
        return sortField;
    }

    public void setSortField(String sortField) {
        this.sortField = sortField;
    }

    public String getSortDir() {
        return sortDir;
    }

    public void setSortDir(String sortDir) {
        this.sortDir = sortDir;
    }

    public String getKeyword() {
        return keyword;
    }

    public void setKeyword(String keyword) {
        this.keyword = keyword;
    }

    public PagingAndSortingHelper(ModelAndViewContainer model,  String listName, String sortField, String sortDir, String keyword){
            this.model = model;
            this.listName = listName;
            this.sortField = sortField;
            this.sortDir =sortDir;
            this.keyword = keyword;
    }

    public void updateModelAttributes(int pageNum, Page<?> page){
        List<?> itemList = page.getContent();
        int pageSize = page.getSize();
        long startCount = (pageNum - 1) * pageSize + 1 ;
        long endCount = startCount + pageSize- 1;
        if (endCount > page.getTotalElements()) {
            endCount = page.getTotalElements();
        }

        model.addAttribute("currentPage", pageNum);
        model.addAttribute("totalPages", page.getTotalPages());
        model.addAttribute("startCount", startCount);
        model.addAttribute("endCount", endCount);
        model.addAttribute("totalItems", page.getTotalElements());
        model.addAttribute(listName, itemList);

    }

    public void listEntities(int pageNum,int pageSize, SearchRepository<?, Integer> repository){
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();

        Pageable pageable = PageRequest.of(pageNum - 1, pageSize, sort);
        Page<?> page = null;

        if (keyword != null) {
            page = repository.findAll(keyword, pageable);
        } else {
            page = repository.findAll(pageable);
        }
        updateModelAttributes(pageNum,page);
    }

    public Pageable createPageable(int pageSize, int pageNum) {
        Sort sort = Sort.by(sortField);
        sort = sortDir.equals("asc") ? sort.ascending() : sort.descending();
        return PageRequest.of(pageNum - 1, pageSize,sort);
    }
}
