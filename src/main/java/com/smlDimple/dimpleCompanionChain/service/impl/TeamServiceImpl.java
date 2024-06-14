package com.smlDimple.dimpleCompanionChain.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.smlDimple.dimpleCompanionChain.model.domain.Team;
import com.smlDimple.dimpleCompanionChain.service.TeamService;
import com.smlDimple.dimpleCompanionChain.mapper.TeamMapper;
import org.springframework.stereotype.Service;

/**
* @author 22504
* @description 针对表【team(队伍)】的数据库操作Service实现
* @createDate 2024-06-14 13:50:28
*/
@Service
public class TeamServiceImpl extends ServiceImpl<TeamMapper, Team>
    implements TeamService{

}




