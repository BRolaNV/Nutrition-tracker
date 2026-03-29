import com.rolan.Main;
import com.rolan.model.usda.USDAFoodSearchResponse;
import com.rolan.service.USDAFoodServiceImpl;
import com.rolan.service.interfaces.USDAFoodService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest(classes = Main.class)
public class USDAFoodServiceImplTest {

    @Autowired
    private USDAFoodService usdaFoodService;

    private USDAFoodSearchResponse usdaFoodSearchResponse;

    @Test
    public void searchFoodTest(){
        usdaFoodSearchResponse = usdaFoodService.searchFood("chicken breast", 10);

        assertNotNull(usdaFoodSearchResponse);
        assertFalse(usdaFoodSearchResponse.getFoods().isEmpty());

        usdaFoodSearchResponse.getFoods().forEach(System.out::println);
    }
}
