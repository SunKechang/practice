package ltd.recommend.cloud.newbee.service.impl;

import ltd.common.cloud.newbee.dto.Result;
import ltd.common.cloud.newbee.util.BeanUtil;
import ltd.goods.cloud.newbee.dto.GoodDTO;
import ltd.goods.cloud.newbee.openfeign.NewBeeCloudGoodsServiceFeign;
import ltd.recommend.cloud.newbee.controller.vo.NewBeeMallIndexConfigGoodsVO;
import ltd.recommend.cloud.newbee.dao.BrowseMapper;
import ltd.recommend.cloud.newbee.entity.Browse;
import ltd.recommend.cloud.newbee.entity.LoginMallUser;
import ltd.recommend.cloud.newbee.service.NewBeeMallBrowseService;
import ltd.user.cloud.newbee.openfeign.NewBeeCloudUserServiceFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class NewBeeMallBrowseServiceImpl implements NewBeeMallBrowseService {

    @Autowired
    BrowseMapper browseMapper;

    @Autowired
    private NewBeeCloudGoodsServiceFeign goodsService;

    private static int selfCommentNum = 10;

    @Override
    public void addBrowse(Browse browse) {
        browseMapper.insert(browse);
    }

    @Override
    public List<NewBeeMallIndexConfigGoodsVO> getRecommendGoodsByUserId(Long userId) {
        //获取浏览记录，将浏览记录按categoryId分类
        List<Browse> browses = browseMapper.getByUserId(userId);
        Map<Long, Integer> cates = new HashMap<>();
        for(Browse browse : browses) {
            Integer origin = cates.getOrDefault(browse.getCategoryId(), 0);
            cates.put(browse.getCategoryId(), origin+1);
        }
        List<NewBeeMallIndexConfigGoodsVO> res = new ArrayList<>(selfCommentNum);
        int used = 0;
        for(Long key : cates.keySet()) {
            if(used >= selfCommentNum) {
                break;
            }
            //获取这一种类浏览记录在所有浏览记录中的比重，获取相应比重的推荐商品
            float num = (float)cates.get(key)/browses.size() * selfCommentNum;
            if(num < 1) {
                num = 1;
            }
            //获取推荐种类的商品
            Result<List<GoodDTO>> listResult = goodsService.listSingleCateGoods(key, (int)num);
            List<NewBeeMallIndexConfigGoodsVO> goodsTemp;
            goodsTemp = BeanUtil.copyList(listResult.getData(), NewBeeMallIndexConfigGoodsVO.class);
            res.addAll(goodsTemp);
            used += goodsTemp.size();
        }
        if(used < selfCommentNum) {
            for(int i=0;used < selfCommentNum && i<100;i++) {
                Result<List<GoodDTO>> listResult = goodsService.listSingleCateGoods((long) i, selfCommentNum-used);
                List<NewBeeMallIndexConfigGoodsVO> goodsTemp;
                goodsTemp = BeanUtil.copyList(listResult.getData(), NewBeeMallIndexConfigGoodsVO.class);
                res.addAll(goodsTemp);
                used += goodsTemp.size();
            }
        }
        return res;
    }


}
