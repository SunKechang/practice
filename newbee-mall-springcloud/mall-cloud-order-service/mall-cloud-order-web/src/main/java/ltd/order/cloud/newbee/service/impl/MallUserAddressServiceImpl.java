
package ltd.order.cloud.newbee.service.impl;

import ltd.common.cloud.newbee.enums.ServiceResultEnum;
import ltd.common.cloud.newbee.exception.NewBeeMallException;
import ltd.common.cloud.newbee.util.BeanUtil;
import ltd.order.cloud.newbee.controller.vo.MallUserAddressVO;
import ltd.order.cloud.newbee.dao.MallUserAddressMapper;
import ltd.order.cloud.newbee.entity.MallUserAddress;
import ltd.order.cloud.newbee.service.MallUserAddressService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Date;
import java.util.List;

@Service
public class MallUserAddressServiceImpl implements MallUserAddressService {

    @Autowired
    private MallUserAddressMapper userAddressMapper;

    @Override
    public List<MallUserAddressVO> getMyAddresses(Long userId) {
        List<MallUserAddress> myAddressList = userAddressMapper.findMyAddressList(userId);
        List<MallUserAddressVO> mallUserAddressVOS = BeanUtil.copyList(myAddressList, MallUserAddressVO.class);
        return mallUserAddressVOS;
    }

    @Override
    @Transactional
    public Boolean saveUserAddress(MallUserAddress mallUserAddress) {
        Date now = new Date();
        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            //添加默认地址，需要将原有的默认地址修改掉
            MallUserAddress defaultAddress = userAddressMapper.getMyDefaultAddress(mallUserAddress.getUserId());
            if (defaultAddress != null) {
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                int updateResult = userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
                if (updateResult < 1) {
                    //未更新成功
                    NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        return userAddressMapper.insertSelective(mallUserAddress) > 0;
    }

    @Override
    public Boolean updateMallUserAddress(MallUserAddress mallUserAddress) {
        MallUserAddress tempAddress = getMallUserAddressById(mallUserAddress.getAddressId());
        Date now = new Date();
        if (mallUserAddress.getDefaultFlag().intValue() == 1) {
            //修改为默认地址，需要将原有的默认地址修改掉
            MallUserAddress defaultAddress = userAddressMapper.getMyDefaultAddress(mallUserAddress.getUserId());
            if (defaultAddress != null && !defaultAddress.getAddressId().equals(tempAddress)) {
                //存在默认地址且默认地址并不是当前修改的地址
                defaultAddress.setDefaultFlag((byte) 0);
                defaultAddress.setUpdateTime(now);
                int updateResult = userAddressMapper.updateByPrimaryKeySelective(defaultAddress);
                if (updateResult < 1) {
                    //未更新成功
                    NewBeeMallException.fail(ServiceResultEnum.DB_ERROR.getResult());
                }
            }
        }
        mallUserAddress.setUpdateTime(now);
        return userAddressMapper.updateByPrimaryKeySelective(mallUserAddress) > 0;
    }

    @Override
    public MallUserAddress getMallUserAddressById(Long addressId) {
        MallUserAddress mallUserAddress = userAddressMapper.selectByPrimaryKey(addressId);
        if (mallUserAddress == null) {
            NewBeeMallException.fail(ServiceResultEnum.DATA_NOT_EXIST.getResult());
        }
        return mallUserAddress;
    }

    @Override
    public MallUserAddress getMyDefaultAddressByUserId(Long userId) {
        return userAddressMapper.getMyDefaultAddress(userId);
    }

    @Override
    public Boolean deleteById(Long addressId) {
        return userAddressMapper.deleteByPrimaryKey(addressId) > 0;
    }
}
