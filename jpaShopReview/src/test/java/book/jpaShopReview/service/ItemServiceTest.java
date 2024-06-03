package book.jpaShopReview.service;

import book.jpaShopReview.domain.Album;
import book.jpaShopReview.exception.NotEnoughStockException;
import book.jpaShopReview.repository.ItemRepository;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@RunWith(SpringRunner.class)
@SpringBootTest
@Transactional
public class ItemServiceTest {
    @Autowired ItemRepository itemRepository;
    @Autowired ItemService itemService;

    @Test
    public void 아이템_저장() throws Exception {

        //given
        Album album = new Album();
        album.setArtist("NewJeans");
        album.setName("omg");

        //when
        itemService.saveItem(album);

        //then
        assertEquals(album, itemRepository.findOne(album.getId()));
    }

    @Test(expected = NotEnoughStockException.class)
    public void 음수_재고() {

        //given
        Album album = new Album();
        album.setArtist("NewJeans");
        album.setName("omg");
        album.addStock(5); // 재고 5개

        //when
        itemService.saveItem(album);

        //then
        album.removeStock(6);
        fail("재고는 음수 값이 될 수 없다.");
    }
}