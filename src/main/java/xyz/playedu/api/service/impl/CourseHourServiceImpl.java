package xyz.playedu.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import xyz.playedu.api.domain.CourseHour;
import xyz.playedu.api.exception.NotFoundException;
import xyz.playedu.api.service.CourseHourService;
import xyz.playedu.api.mapper.CourseHourMapper;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author tengteng
 * @description 针对表【course_hour】的数据库操作Service实现
 * @createDate 2023-03-15 10:16:45
 */
@Service
public class CourseHourServiceImpl extends ServiceImpl<CourseHourMapper, CourseHour> implements CourseHourService {

    @Override
    public CourseHour findOrFail(Integer id, Integer courseId) throws NotFoundException {
        CourseHour hour = getOne(query().getWrapper().eq("id", id).eq("course_id", courseId));
        if (hour == null) {
            throw new NotFoundException("课时不存在");
        }
        return hour;
    }

    @Override
    public void update(CourseHour courseHour, Integer chapterId, Integer sort, String title, Integer duration) {
        CourseHour hour = new CourseHour();
        hour.setId(courseHour.getId());
        hour.setChapterId(chapterId);
        hour.setSort(sort);
        hour.setTitle(title);
        hour.setDuration(duration);

        updateById(hour);
    }

    @Override
    public List<CourseHour> getHoursByCourseId(Integer courseId) {
        return list(query().getWrapper().eq("course_id", courseId).orderByAsc("sort"));
    }

    @Override
    public CourseHour create(Integer courseId, Integer chapterId, Integer sort, String title, String type, Integer rid, Integer duration) {
        CourseHour hour = new CourseHour();
        hour.setCourseId(courseId);
        hour.setChapterId(chapterId);
        hour.setSort(sort);
        hour.setTitle(title);
        hour.setType(type);
        hour.setRid(rid);
        hour.setDuration(duration);
        hour.setCreatedAt(new Date());

        save(hour);

        return hour;
    }

    @Override
    public Integer getCountByCourseId(Integer courseId) {
        return Math.toIntExact(count(query().getWrapper().eq("course_id", courseId)));
    }

    @Override
    public Integer getCountByChapterId(Integer chapterId) {
        return Math.toIntExact(count(query().getWrapper().eq("chapter_id", chapterId)));
    }

    @Override
    public void remove(Integer courseId, Integer chapterId) {
        remove(query().getWrapper().eq("course_id", courseId).eq("chapter_id", chapterId));
    }

    @Override
    public void updateSort(List<Integer> ids, Integer cid) {
        if (ids == null || ids.size() == 0) {
            return;
        }
        List<CourseHour> hours = new ArrayList<>();
        final Integer[] sortVal = {0};
        for (Integer idVal : ids) {
            hours.add(new CourseHour() {{
                setId(idVal);
                setCourseId(cid);
                setSort(sortVal[0]++);
            }});
        }
        updateBatchById(hours);
    }

    @Override
    public List<Integer> getRidsByCourseId(Integer courseId, String type) {
        return list(query().getWrapper().eq("course_id", courseId).eq("type", type)).stream().map(CourseHour::getRid).toList();
    }

    @Override
    public List<CourseHour> chunk(List<Integer> hourIds) {
        return list(query().getWrapper().in("id", hourIds));
    }
}




