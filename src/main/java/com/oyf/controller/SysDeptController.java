package com.oyf.controller;

import com.oyf.dto.SysDeptLevelDto;
import com.oyf.param.DeptParam;
import com.oyf.service.SysDeptService;
import com.oyf.tree.SysDeptTreeService;
import com.oyf.utils.JsonData;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.servlet.ModelAndView;

import javax.annotation.Resource;
import java.util.List;


/**
 * Create Time: 2019年03月21日 15:41
 * Create Author: 欧阳飞
 **/

@Controller
@RequestMapping("/sys/dept")
public class SysDeptController {

    @Resource
    private SysDeptTreeService sysTreeService;

    @Resource
    private SysDeptService sysDeptService;


    @RequestMapping("/dept.page")
    public ModelAndView entry(){

        return new ModelAndView("dept");

    }

    @RequestMapping("/save.json")
    @ResponseBody
    public JsonData save(DeptParam deptParam){

        sysDeptService.save(deptParam);

        return JsonData.success();
    }

    @RequestMapping("/tree.json")
    @ResponseBody
    public JsonData deptTree(){

        List<SysDeptLevelDto> deptList = sysTreeService.getDeptTree();

        return JsonData.success(deptList);
    }

    @RequestMapping("/update.json")
    @ResponseBody
    public JsonData update(DeptParam deptParam){

        sysDeptService.update(deptParam);

        return JsonData.success();
    }

    @RequestMapping("/delete.json")
    @ResponseBody
    public JsonData delete(Integer id){

        sysDeptService.delete(id);

        return JsonData.success();
    }



}
