package com.smlDimple.dimpleCompanionChain.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smlDimple.dimpleCompanionChain.common.BaseResponse;
import com.smlDimple.dimpleCompanionChain.common.ErrorCode;
import com.smlDimple.dimpleCompanionChain.exception.BusinessException;
import com.smlDimple.dimpleCompanionChain.mapper.UserTeamMapper;
import com.smlDimple.dimpleCompanionChain.model.domain.Team;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import com.smlDimple.dimpleCompanionChain.model.domain.UserTeam;
import com.smlDimple.dimpleCompanionChain.model.enums.TeamStatusEnum;
import com.smlDimple.dimpleCompanionChain.service.TeamService;
import com.smlDimple.dimpleCompanionChain.mapper.TeamMapper;
import com.smlDimple.dimpleCompanionChain.service.UserTeamService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.lang.annotation.Retention;
import java.util.Date;
import java.util.Optional;

import static com.smlDimple.dimpleCompanionChain.model.enums.TeamStatusEnum.ENCRYPT;
import static com.smlDimple.dimpleCompanionChain.model.enums.TeamStatusEnum.PUBLICITY;

/**
* @author 22504
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-06-14 13:50:28
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{


    @Resource
    private UserTeamService userTeamService;


    @Override
    public long addTeam(Team team, User loginUser) {
//请求参数是否为空？
        if(team == null ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR);
        }
//是否登录，未登录不允许创建
        if(loginUser == null){
            throw new BusinessException(ErrorCode.NOT_LOGIN);
        }
        final long userId = loginUser.getId();
//校验信息队伍人数> 1且<=20
         int maxNum = Optional.ofNullable(team.getMaxNum()).orElse(0);
        if(maxNum < 1 || maxNum > 20){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍人数不合法");
        }
//队伍标题<=20
        String name = team.getName();
        if(name.length() > 20 && StringUtils.isBlank(name)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍标题不合法");
        }
//描述<=512
        String decription = team.getDescription();
        if( StringUtils.isNotBlank(decription) || decription.length() > 512 ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍描述不合法");
        }
//status是否公开（int) 不传参数默认为0（公开） 0 - 公开，1 - 私有，2 - 加密
        int status = Optional.ofNullable(team.getStatus()).orElse(0);
        TeamStatusEnum statusEnum = TeamStatusEnum.getEnumByValue(status);
        if(statusEnum == null ){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"队伍状态不合法");
        }

//staus是加密状态，一定要有密码，并且密码<=32


            String password = team.getPassword();
//            判断是否为加密状态,如果加密但是密码为空或者大于32抛出异常
            if(ENCRYPT.equals(statusEnum) && (StringUtils.isBlank(password) || password.length() > 32)){
                throw new BusinessException(ErrorCode.PARAMS_ERROR,"密码未设置或长度过长");
            }



//过期时间>当前时间
        Date expireTime = team.getExpireTime();
        if( new Date().after(expireTime)){
            throw new BusinessException(ErrorCode.PARAMS_ERROR ,"过期时间不合法");
        }

//校验用户最多创建5个队伍
        //查询用户创建的队伍数量
        // todo bug 可能同时创建100个队伍，如果用户快速点击
        QueryWrapper <Team> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("userId",userId);
        long hasTeamNum = this.count(queryWrapper);
        if (hasTeamNum >= 5){
            throw new BusinessException(ErrorCode.PARAMS_ERROR,"用户最多创建5个队伍");
        }
//插入用户列表到队伍表
        team.setId(null);
        team.setUserId(userId);
        //todo 这里要保证插入用户列表和队伍表是原子操作
        boolean result = this.save(team);
        if(!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建队伍失败");
        }
//插入用户 => 队伍关系到关系表
        UserTeam userTeam = new UserTeam();
        userTeam.setUserId(userId);
        userTeam.setTeamId(team.getId());
        userTeam.setJoinTime(new Date());
        result = userTeamService.save(userTeam);
        if (!result){
            throw new BusinessException(ErrorCode.SYSTEM_ERROR,"创建队伍失败");
        }

        return 0;
    }
}




