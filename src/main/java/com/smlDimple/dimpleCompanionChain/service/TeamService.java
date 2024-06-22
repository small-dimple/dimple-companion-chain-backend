package com.smlDimple.dimpleCompanionChain.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.smlDimple.dimpleCompanionChain.model.domain.Team;
import com.smlDimple.dimpleCompanionChain.model.domain.User;
import com.smlDimple.dimpleCompanionChain.model.dto.TeamQuery;
import com.smlDimple.dimpleCompanionChain.model.request.TeamJoinRequest;
import com.smlDimple.dimpleCompanionChain.model.request.TeamQuitRequest;
import com.smlDimple.dimpleCompanionChain.model.request.TeamUpdateRequest;
import com.smlDimple.dimpleCompanionChain.model.vo.TeamUserVO;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

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

    /**
     * 获取队伍列表(搜索队伍）
     *
     * @param teamQuery
     * @return
     */
    List<TeamUserVO> listTeams(TeamQuery teamQuery, boolean isAdmin);

    /**
     * 更新队伍信息
     *
     * @param teamUpdateRequest
     * @param loginUser
     * @return
     */
    boolean updateTeam(TeamUpdateRequest teamUpdateRequest,User loginUser);

    /**
     * 加入队伍
     *
     * @param teamJoinRequest
     * @param loginUser
     * @return
     */
    boolean joinTeam(TeamJoinRequest teamJoinRequest, User loginUser);

    /**
     * 退出队伍
     *
     * @param teamQuitRequest
     * @param loginUser
     * @return
     */
    boolean quitTeam(TeamQuitRequest teamQuitRequest, User loginUser);

    /**
     * 删除队伍
     *
     * @param id
     * @return
     */
    boolean deleteTeam(long id, User loginUser);
}
