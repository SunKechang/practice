
package ltd.goods.cloud.newbee.dto;

import java.util.List;

public class UpdateStockNumDTO {

    private List<StockNumDTO> stockNumDTOS;

    public List<StockNumDTO> getStockNumDTOS() {
        return stockNumDTOS;
    }

    public void setStockNumDTOS(List<StockNumDTO> stockNumDTOS) {
        this.stockNumDTOS = stockNumDTOS;
    }
}
