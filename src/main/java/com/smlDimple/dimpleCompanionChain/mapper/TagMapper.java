package com.smlDimple.dimpleCompanionChain.mapper;

import com.smlDimple.dimpleCompanionChain.model.domain.Tag;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 22504
* @description 针对表【tag(标签)】的数据库操作Mapper
* @createDate 2024-06-03 23:02:32
* @Entity com.smlDimple.dimpleCompanionChain.model.domain.Tag
*/
@Mapper
public interface TagMapper extends BaseMapper<Tag> {

}




