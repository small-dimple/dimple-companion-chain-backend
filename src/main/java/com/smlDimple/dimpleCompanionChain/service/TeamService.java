package com.smlDimple.dimpleCompanionChain.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.smlDimple.dimpleCompanionChain.model.domain.Team;
import com.smlDimple.dimpleCompanionChain.model.domain.User;

/**
* @author small-dimple
* @description 针对表【team(队伍)】的数据库操作Service
* @createDate 2024-06-14 13:50:28
*/
public interface TeamService extends IService<Team> {


    /**
     * 创建队伍
     *
     * @param team
     * @param loginUser
     * @return
     */
    long addTeam(Team team, User loginUser);
}
