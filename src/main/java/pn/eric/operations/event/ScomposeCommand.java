package pn.eric.operations.event;

/**
 * @author duwupeng
 * @date
 */
public enum  ScomposeCommand {
//    1. cd scompose & git pull & git checkout F_XHT_2.0.0
//            3. ./scompose s-pull
//    2. ./scompose s-rebuild [-Pproduction]
//    3. ./scompose s-docker-push
//    可选:
//    4. ./scompose s-diff (optional)
//    5. ./scompose s-log (optional)
//    6. 登录灰度服务器: ssh isuwang@gray
//    7. cd scompose & git pull & git checkout F_XHT_2.0.0
//            10. ./scompose up -d
    pull,
    checkout,
    spull,
    srebuild,
    sDockerPush,
    sDiff,
    sLog,
    up
}
