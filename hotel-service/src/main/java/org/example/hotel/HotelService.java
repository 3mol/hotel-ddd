package org.example.hotel;

import javax.annotation.Resource;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class HotelService {
  @Resource HotelRepository hotelRepository;

  // 营业
  @Transactional
  public void open(Long id) {
    // ...
    final Hotel hotel = hotelRepository.getOne(id);
    hotel.setStatus(HotelStatus.OPEN);
    hotelRepository.save(hotel);
  }

  /** 歇业 */
  @Transactional
  public void close(Long id) {
    // ...
    final Hotel hotel = hotelRepository.getOne(id);
    hotel.setStatus(HotelStatus.CLOSE);
    hotelRepository.save(hotel);
  }

  /**
   * 添加酒店
   *
   * @param hotel ~
   */
  @Transactional(rollbackFor = Exception.class)
  public HotelResp append(AppendHotelReq hotel) {
    // 工厂
    final Hotel addHotel =
        new Hotel(
            null,
            hotel.getName(),
            hotel.getAddress(),
            hotel.getPhone(),
            HotelStatus.CLOSE,
            hotel.getDescription());
    final Hotel save = hotelRepository.save(addHotel);
    // 转换
    return convert(save);
  }

  private HotelResp convert(Hotel save) {
    final HotelResp hotelResp = new HotelResp();
    hotelResp.setId(save.getId());
    hotelResp.setName(save.getName());
    hotelResp.setAddress(save.getAddress());
    hotelResp.setPhone(save.getPhone());
    hotelResp.setStatus(save.getStatus());
    hotelResp.setDescription(save.getDescription());
    return hotelResp;
  }

  public HotelResp getOne(Long id) {
    return convert(hotelRepository.getOne(id));
  }
}
