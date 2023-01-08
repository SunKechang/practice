package ltd.recommend.cloud.newbee.dao;

import ltd.recommend.cloud.newbee.entity.Browse;
import org.apache.ibatis.annotations.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BrowseMapper {

    public void insert(Browse browse);

    public List<Browse> getByUserId(@Param("userId")Long userId);
}
