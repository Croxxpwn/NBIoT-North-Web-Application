package com.croxx.nbiot.controller.v1;


import com.croxx.nbiot.model.Alarm;
import com.croxx.nbiot.model.JwtUser;
import com.croxx.nbiot.model.User;
import com.croxx.nbiot.model.UserRepository;
import com.croxx.nbiot.request.ReqAlarmHandler;
import com.croxx.nbiot.response.ResAlarm;
import com.croxx.nbiot.response.ResJwtAccessToken;
import com.croxx.nbiot.response.ResMsg;
import com.croxx.nbiot.service.NBIoTAlarmService;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.ConstraintViolation;
import javax.validation.ConstraintViolationException;
import javax.validation.Valid;
import javax.validation.constraints.Size;
import java.util.List;
import java.util.Set;

@RestController
@PreAuthorize("hasRole('USER')")
@RequestMapping("/v1/alarm")
@Validated
public class AlarmControllerV1 {

    @Autowired
    private UserRepository userRepository;
    @Autowired
    private NBIoTAlarmService nbIoTAlarmService;

    @ApiOperation(value = "查询设备发出的所有警报", notes = "使用deviceId查询该设备发出的全部警报")
    @RequestMapping(value = "/device/{deviceId}/", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<List<ResAlarm>>> queryAlarmsByDeivce(@PathVariable @Size(min = 6, max = 48) String deviceId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        List<ResAlarm> resAlarms = nbIoTAlarmService.getResAlarmsByDeviceIdAndStatus(deviceId, user, Alarm.STATUS_ALL);
        if (resAlarms != null) {
            return ResponseEntity.ok(new ResMsg<List<ResAlarm>>(resAlarms, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<List<ResAlarm>>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "查询设备发出的所有已经解决的警报", notes = "使用deviceId查询该设备发出的已经解决警报")
    @RequestMapping(value = "/device/{deviceId}/solved", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<List<ResAlarm>>> querySolvedAlarmsByDeivce(@PathVariable @Size(min = 6, max = 48) String deviceId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        List<ResAlarm> resAlarms = nbIoTAlarmService.getResAlarmsByDeviceIdAndStatus(deviceId, user, Alarm.STATUS_SOLVED);
        if (resAlarms != null) {
            return ResponseEntity.ok(new ResMsg<List<ResAlarm>>(resAlarms, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<List<ResAlarm>>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "查询设备发出的所有未解决警报", notes = "使用deviceId查询该设备发出的未解决警报")
    @RequestMapping(value = "/device/{deviceId}/unsolved", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<List<ResAlarm>>> queryUnsolvedAlarmsByDeivce(@PathVariable @Size(min = 6, max = 48) String deviceId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        List<ResAlarm> resAlarms = nbIoTAlarmService.getResAlarmsByDeviceIdAndStatus(deviceId, user, Alarm.STATUS_UNSOLVED);
        if (resAlarms != null) {
            return ResponseEntity.ok(new ResMsg<List<ResAlarm>>(resAlarms, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<List<ResAlarm>>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "查询用户所有设备下的所有警报", notes = "查询用户所有设备下的所有警报")
    @RequestMapping(value = "/", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<List<ResAlarm>>> queryAlarms() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        List<ResAlarm> resAlarms = nbIoTAlarmService.getResAlarmsByUserAndStatus(user, Alarm.STATUS_ALL);
        if (resAlarms != null) {
            return ResponseEntity.ok(new ResMsg<List<ResAlarm>>(resAlarms, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<List<ResAlarm>>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "查询用户所有设备下的所有已经解决的警报", notes = "查询用户所有设备下的所有已经解决的警报")
    @RequestMapping(value = "/solved", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<List<ResAlarm>>> querySolvedAlarms() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        List<ResAlarm> resAlarms = nbIoTAlarmService.getResAlarmsByUserAndStatus(user, Alarm.STATUS_SOLVED);
        if (resAlarms != null) {
            return ResponseEntity.ok(new ResMsg<List<ResAlarm>>(resAlarms, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<List<ResAlarm>>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "查询用户所有设备下的所有未解决的警报", notes = "查询用户所有设备下的所有未解决的警报")
    @RequestMapping(value = "/unsolved", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<List<ResAlarm>>> queryUnsolvedAlarms() {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        List<ResAlarm> resAlarms = nbIoTAlarmService.getResAlarmsByUserAndStatus(user, Alarm.STATUS_UNSOLVED);
        if (resAlarms != null) {
            return ResponseEntity.ok(new ResMsg<List<ResAlarm>>(resAlarms, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<List<ResAlarm>>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "查询警报详细信息", notes = "查询警报详细信息")
    @RequestMapping(value = "/detail/{alarmId}", method = RequestMethod.GET)
    public ResponseEntity<ResMsg<ResAlarm>> getAlarmDetail(@PathVariable("alarmId") Long alarmId) {
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        ResAlarm resAlarm = nbIoTAlarmService.getResAlarmByAlarmId(alarmId, user);
        if (resAlarm != null) {
            return ResponseEntity.ok(new ResMsg<ResAlarm>(resAlarm, ResMsg.MSG_SUCCESS));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg<ResAlarm>(NBIoTAlarmService.QUERY_DEVICEID_NOT_FOUND_OR_DENIED));
        }
    }

    @ApiOperation(value = "处理警报", notes = "处理警报并填充警报信息")
    @RequestMapping(value = "/solve", method = RequestMethod.POST)
    public ResponseEntity<ResMsg> solveAlarm(@Valid @RequestBody ReqAlarmHandler reqAlarmHandler, BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            List<String> msgs = ResMsg.getBindErrorsMessage(bindingResult);
            return ResponseEntity.badRequest().body(new ResMsg<ResJwtAccessToken>(msgs));
        }
        JwtUser jwtUser = (JwtUser) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        User user = userRepository.findByEmail(jwtUser.getUsername());
        String status = nbIoTAlarmService.handleAlarmByAlarmId(reqAlarmHandler.getId(), user, reqAlarmHandler.getType(), reqAlarmHandler.getRisk());
        if (status.equals(NBIoTAlarmService.HANDLE_SUCCESS)) {
            return ResponseEntity.ok(new ResMsg(status));
        } else {
            return ResponseEntity.badRequest().body(new ResMsg(status));
        }
    }

    @ExceptionHandler(value = {ConstraintViolationException.class})
    @ResponseStatus(value = HttpStatus.BAD_REQUEST)
    public ResponseEntity<ResMsg> handleResourceNotFoundException(ConstraintViolationException e) {
        Set<ConstraintViolation<?>> violations = e.getConstraintViolations();
        StringBuilder strBuilder = new StringBuilder();
        for (ConstraintViolation<?> violation : violations) {
            strBuilder.append(violation.getMessage() + "\n");
        }
        return ResponseEntity.badRequest().body(new ResMsg(strBuilder.toString()));
    }

}
