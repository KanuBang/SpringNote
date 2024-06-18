package book.jpaShopAPI.service;

import book.jpaShopAPI.domain.Item;
import book.jpaShopAPI.repository.ItemRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@Transactional(readOnly = true)
@RequiredArgsConstructor
public class ItemService {

    private final ItemRepository itemRepository;

    // 변경 감지 기능 사용
    // 영속성 컨텍스트에서 엔티티를 다시 조회한 후에 데이터를 수정하는 방법
    // 사실상 merge 구현 코드 -> 마지막에 return findItem
    @Transactional
    public void updateItem(Long itemId, String name, int price, int stockQuantity){
        // 트랜잭션 안에서 엔티티를 다시 조회, 변경할 값 선택 트랜잭션 커밋 시점에 변경 감지(Dirty Checking)이 동작해서
        // 데이터베이스에 UPDATE SQL 실행
        Item findItem = itemRepository.findOne(itemId);
        findItem.setPrice(price);
        findItem.setName(name);
        findItem.setStockQuantity(stockQuantity);

    }
    @Transactional
    public void saveItem(Item item) {
        System.out.println("repository2222");
        itemRepository.save(item);
    }

    public List<Item> findItems() {
        return itemRepository.findAll();
    }

    public Item findOne(Long itemId) {
        return itemRepository.findOne(itemId);
    }
}
