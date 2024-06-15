package com.smlDimple.dimpleCompanionChain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smlDimple.dimpleCompanionChain.common.BaseResponse;
import com.smlDimple.dimpleCompanionChain.common.ErrorCode;
import com.smlDimple.dimpleCompanionChain.common.ResultUtils;
import com.smlDimple.dimpleCompanionChain.exception.BusinessException;
import com.smlDimple.dimpleCompanionChain.model.domain.Team;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import com.smlDimple.dimpleCompanionChain.model.dto.TeamQuery;
import com.smlDimple.dimpleCompanionChain.model.request.TeamAddRequest;
import com.smlDimple.dimpleCompanionChain.model.request.TeamUpdateRequest;
import com.smlDimple.dimpleCompanionChain.model.vo.TeamUserVO;
import com.smlDimple.dimpleCompanionChain.service.TeamService;
import com.smlDimple.dimpleCompanionChain.service.UserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.List;

/**
 * @Author: small-dimple
 **/
@RestController
@RequestMapping("/team")
//@CrossOrigin可以解决跨域问题，origins配置可以跨域访问的地址，但是只能防止前端向你发送请求
//todo 跨域问题有待学习
@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;



    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody TeamAddRequest teamAddRequest , HttpServletRequest request){
        if(teamAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody long id){
        if(id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean result = teamService.removeById(id);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }


    @PostMapping("/update")
    public BaseResponse<Boolean> updateTeam(@RequestBody TeamUpdateRequest teamUpdateRequest , HttpServletRequest request){
        if(teamUpdateRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.updateTeam(teamUpdateRequest, loginUser);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "修改失败");
        }
        return ResultUtils.success(true);
    }

    @GetMapping("/get")
    public BaseResponse<Team> getTeamById(@RequestParam long id){
        if(id < 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Team team = teamService.getById(id);
        if(team == null){
            throw new BusinessException(ErrorCode.NULL_ERROR, "队伍不存在");
        }
        return ResultUtils.success(team);
    }



    @GetMapping("/list")
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery , HttpServletRequest request) {
        if(teamQuery == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery , isAdmin);
        return ResultUtils.success(teamList);
    }

    @GetMapping("/list/page")
    public BaseResponse<Page<Team>> listTeamByPage(TeamQuery teamQuery) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long current = teamQuery.getPageNum();
        long pageSize = teamQuery.getPageSize();
        if (current < 0 || pageSize < 0) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        Page<Team> page = new Page<>(current, pageSize);
        Team team = new Team();
        BeanUtils.copyProperties(teamQuery,team );

        QueryWrapper<Team> queryWrapper = new QueryWrapper<>(team);
        Page<Team> resultPage = teamService.page(page, queryWrapper);
        return ResultUtils.success(resultPage);
    }
}
