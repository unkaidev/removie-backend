package vn.removie.movies.Service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import vn.removie.movies.Entity.Wishlist;
import vn.removie.movies.Repository.WishlistRepository;

@Service
@RequiredArgsConstructor
public class WishListService {
    @Autowired
    public WishlistRepository wishlistRepository;


    public void saveWishList(Wishlist newWishlist) {
        wishlistRepository.save(newWishlist);
    }
}
