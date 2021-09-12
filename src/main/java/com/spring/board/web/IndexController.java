package com.spring.board.web;

import com.spring.board.config.auth.dto.SessionUser;
import com.spring.board.service.posts.PostsService;
import com.spring.board.web.dto.PageListDto;
import com.spring.board.web.dto.PostsListResponseDto;
import com.spring.board.web.dto.PostsResponseDto;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import javax.servlet.http.HttpSession;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Controller
public class IndexController {

    private final int DEFAULT_POSTS_COUNT_PER_PAGE = 10;
    private final int DISPLAY_PAGE_COUNT = 5;
    private final PostsService postsService;
    private final HttpSession httpSession;

    @GetMapping("/")
    public String index(@PageableDefault(size = DEFAULT_POSTS_COUNT_PER_PAGE, sort = "id",
            direction = Sort.Direction.DESC) Pageable pageable, Model model) {

        Page<PostsListResponseDto> page = postsService.findAll(pageable);
        List<PostsListResponseDto> posts = page.getContent();

        int currentPage = page.getNumber();
        int lastPage = page.getTotalPages() - 1;
        int basePage = (currentPage / DISPLAY_PAGE_COUNT) * DISPLAY_PAGE_COUNT;
        int prevPage = basePage - DISPLAY_PAGE_COUNT >= 0 ? basePage - DISPLAY_PAGE_COUNT : 0;
        int nextPage = basePage + DISPLAY_PAGE_COUNT <= lastPage ? basePage + DISPLAY_PAGE_COUNT : lastPage;

        List<PageListDto> pageList = createPageList(currentPage, lastPage, basePage);

        model.addAttribute("firstPage", 0);
        model.addAttribute("lastPage", lastPage);
        model.addAttribute("prevPage", prevPage);
        model.addAttribute("nextPage", nextPage);
        model.addAttribute("pageList", pageList);
        model.addAttribute("posts", posts);

        SessionUser user = (SessionUser) httpSession.getAttribute("user");

        if (user != null) {
            model.addAttribute("userName", user.getName());
        }

        return "index";
    }

    private List<PageListDto> createPageList(int currentPage, int lastPage, int basePage) {
        List<PageListDto> pageList = new ArrayList<>();

        for (int i = 0; i < DISPLAY_PAGE_COUNT; i++) {
            if (basePage + i > lastPage) {
                break;
            }

            pageList.add(new PageListDto(basePage + i == currentPage, basePage + i));
        }
        return pageList;
    }

    @GetMapping("/posts/save")
    public String postsSave() {
        return "posts-save";
    }

    @GetMapping("/posts/{id}")
    public String postsView(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-view";
    }

    @GetMapping("/posts/update/{id}")
    public String postsUpdate(@PathVariable Long id, Model model) {
        PostsResponseDto dto = postsService.findById(id);
        model.addAttribute("post", dto);

        return "posts-update";
    }
}
