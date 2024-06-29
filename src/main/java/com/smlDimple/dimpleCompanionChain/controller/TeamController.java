package com.smlDimple.dimpleCompanionChain.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.smlDimple.dimpleCompanionChain.common.BaseResponse;
import com.smlDimple.dimpleCompanionChain.common.DeleteRequest;
import com.smlDimple.dimpleCompanionChain.common.ErrorCode;
import com.smlDimple.dimpleCompanionChain.common.ResultUtils;
import com.smlDimple.dimpleCompanionChain.exception.BusinessException;
import com.smlDimple.dimpleCompanionChain.model.domain.Team;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import com.smlDimple.dimpleCompanionChain.model.domain.UserTeam;
import com.smlDimple.dimpleCompanionChain.model.dto.TeamQuery;
import com.smlDimple.dimpleCompanionChain.model.request.TeamAddRequest;
import com.smlDimple.dimpleCompanionChain.model.request.TeamJoinRequest;
import com.smlDimple.dimpleCompanionChain.model.request.TeamQuitRequest;
import com.smlDimple.dimpleCompanionChain.model.request.TeamUpdateRequest;
import com.smlDimple.dimpleCompanionChain.model.vo.TeamUserVO;
import com.smlDimple.dimpleCompanionChain.service.TeamService;
import com.smlDimple.dimpleCompanionChain.service.UserService;
import com.smlDimple.dimpleCompanionChain.service.UserTeamService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

/**
 * @Author: small-dimple
 **/
@RestController
@RequestMapping("/team")
//@CrossOrigin可以解决跨域问题，origins配置可以跨域访问的地址，但是只能防止前端向你发送请求
//todo 跨域问题有待学习
//@CrossOrigin(origins = "http://localhost:5173")
@Slf4j
public class TeamController {

    @Resource
    private TeamService teamService;

    @Resource
    private UserService userService;

    @Resource
    private UserTeamService userTeamService;



    @PostMapping("/add")
    public BaseResponse<Long> addTeam(@RequestBody  TeamAddRequest teamAddRequest , HttpServletRequest request){
        if(teamAddRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        Team team = new Team();
        BeanUtils.copyProperties(teamAddRequest, team);
        long teamId = teamService.addTeam(team, loginUser);
        return ResultUtils.success(teamId);
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
    public BaseResponse<List<TeamUserVO>> listTeams(TeamQuery teamQuery, HttpServletRequest request) {
        if (teamQuery == null) {
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        boolean isAdmin = userService.isAdmin(request);
        // 1、查询队伍列表
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery, isAdmin);
        if(CollectionUtils.isNotEmpty(teamList)) {
            final List<Long> teamIdList = teamList.stream().map(TeamUserVO::getId).collect(Collectors.toList());


            // 2、判断当前用户是否已加入队伍
            QueryWrapper<UserTeam> userTeamQueryWrapper = new QueryWrapper<>();
            try {
                User loginUser = userService.getLoginUser(request);
                userTeamQueryWrapper.eq("userId", loginUser.getId());
                userTeamQueryWrapper.in("teamId", teamIdList);
                List<UserTeam> userTeamList = userTeamService.list(userTeamQueryWrapper);
                // 已加入的队伍 id 集合
                Set<Long> hasJoinTeamIdSet = userTeamList.stream().map(UserTeam::getTeamId).collect(Collectors.toSet());
                teamList.forEach(team -> {
                    boolean hasJoin = hasJoinTeamIdSet.contains(team.getId());
                    team.setHasJoin(hasJoin);
                });
            } catch (Exception e) {
            }
            // 3、查询已加入队伍的人数
            QueryWrapper<UserTeam> userTeamJoinQueryWrapper = new QueryWrapper<>();
            userTeamJoinQueryWrapper.in("teamId", teamIdList);
            List<UserTeam> userTeamList = userTeamService.list(userTeamJoinQueryWrapper);
            // 队伍 id => 加入这个队伍的用户列表
            Map<Long, List<UserTeam>> teamIdUserTeamList = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
            teamList.forEach(team -> team.setHasJoinNum(teamIdUserTeamList.getOrDefault(team.getId(), new ArrayList<>()).size()));
        }
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

    @PostMapping("/join")
    public BaseResponse<Boolean> joinTeam(@RequestBody TeamJoinRequest teamJoinRequest , HttpServletRequest request){
        if(teamJoinRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.joinTeam(teamJoinRequest, loginUser);
        return ResultUtils.success(result);
    }


    @PostMapping("/quit")
    public BaseResponse<Boolean> quitTeam(@RequestBody TeamQuitRequest teamQuitRequest , HttpServletRequest request){
        if(teamQuitRequest == null){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.quitTeam(teamQuitRequest, loginUser);
        return ResultUtils.success(result);
    }


    @PostMapping("/delete")
    public BaseResponse<Boolean> deleteTeam(@RequestBody DeleteRequest deleteRequest, HttpServletRequest request){

        if(deleteRequest == null || deleteRequest.getId() <= 0){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        long id = deleteRequest.getId();
        User loginUser = userService.getLoginUser(request);
        boolean result = teamService.deleteTeam(id,loginUser);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR, "删除失败");
        }
        return ResultUtils.success(true);
    }

    /**
     *获取我创建的队伍列表
     */
    @GetMapping("/list/my/create")
    public BaseResponse<List<TeamUserVO>> listMyCreateTeams(TeamQuery teamQuery , HttpServletRequest request) {
        if(teamQuery == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        teamQuery.setUserId(loginUser.getId());
        boolean isAdmin = userService.isAdmin(loginUser);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery , isAdmin);
        return ResultUtils.success(teamList);
    }

    /**
     * 获取我加入的队伍列表
     */

    @GetMapping("/list/my/join")
    public BaseResponse<List<TeamUserVO>> listMyJoinTeams(TeamQuery teamQuery , HttpServletRequest request) {
        if(teamQuery == null){
            throw  new BusinessException(ErrorCode.PARAMS_ERROR);
        }
        User loginUser = userService.getLoginUser(request);
        QueryWrapper <UserTeam> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",loginUser.getId());
            List <UserTeam> userTeamList = userTeamService.list(queryWrapper);
        Map<Long, List<UserTeam>> listMap = userTeamList.stream().collect(Collectors.groupingBy(UserTeam::getTeamId));
        List<Long> idList = new ArrayList<>(listMap.keySet());
        teamQuery.setIdList(idList);
        boolean isAdmin = userService.isAdmin(loginUser);
        List<TeamUserVO> teamList = teamService.listTeams(teamQuery , isAdmin);
        return ResultUtils.success(teamList);
    }


}
