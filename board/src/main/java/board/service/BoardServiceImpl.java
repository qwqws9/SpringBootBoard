package board.service;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.CollectionUtils;
import org.springframework.web.multipart.MultipartHttpServletRequest;

import board.common.FileUtils;
import board.dto.BoardDto;
import board.dto.BoardFileDto;
import board.mapper.BoardMapper;
import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
//@Transactional
/*
 *  트랜잭션 어노테이션으로 원하는 클래스 또는 메서드 단위로 트랜잭션을 설정할 수 있는 장점이 있지만
 *  새로운 클래스 또는 메서드를 만들때마다 어노테이션을 작성해야하므로
 *  그 숫자가 많아질수록 누락 또는 일관되지 않게 적용될 수 도있으므로 AOP 기능을 이용해 트랜잭션 처리
 *  
 *  그러나 AOP로 트랜잭션을 적용시켰을때 단점은
 *  필요없는곳까지 트랜잭션이 적용되므로 성능에 영향을 미치고
 *  원하는 곳에만 적용하기 어렵기에 일부 에러가 발생한 시점까지의 데이터만 저장되어야할 경우에도
 *  롤백이 되어버린다.
 */
public class BoardServiceImpl implements BoardService {

	@Autowired
	private BoardMapper boardMapper;
	
	@Autowired
	private FileUtils fileUtils;
	

	@Override
	public List<BoardDto> selectBoardList() throws Exception {
		return boardMapper.selectBoardList();
	}

	@Override
	public void insertBoard(BoardDto board, MultipartHttpServletRequest multipartHttpServletRequest) throws Exception {
		boardMapper.insertBoard(board);
		
		List<BoardFileDto> list = fileUtils.parseFileInfo(board.getBoardIdx(), multipartHttpServletRequest);
		
		if(CollectionUtils.isEmpty(list) == false) {
			boardMapper.insertBoardFileList(list);
		}
		
		
		
		
//		if(ObjectUtils.isEmpty(multipartHttpServletRequest) == false) {
//			Iterator<String> iterator = multipartHttpServletRequest.getFileNames();
//			String name;
//			while(iterator.hasNext()) {
//				name = iterator.next();
//				log.debug("file tag name : " + name);
//				List<MultipartFile> list = multipartHttpServletRequest.getFiles(name);
//				for(MultipartFile multipartFile : list) {
//					log.debug("start file information");
//					log.debug("file name : " + multipartFile.getOriginalFilename());
//					log.debug("file size : " + multipartFile.getSize());
//					log.debug("file content type : " + multipartFile.getContentType());
//					log.debug("end file information. \n");
//				}
//			}
//		}
	}

	@Override
	public BoardDto selectBoardDetail(int boardIdx) throws Exception {
		BoardDto board = boardMapper.selectBoardDetail(boardIdx);
		List<BoardFileDto> fileList = boardMapper.selectBoardFileList(boardIdx);
		board.setFileList(fileList);
		boardMapper.updateHitCount(boardIdx);
		return board;
	}

	@Override
	public void updateBoard(BoardDto board) throws Exception {
		boardMapper.updateBoard(board);
	}

	@Override
	public void deleteBoard(int boardIdx) throws Exception {
		boardMapper.deleteBoard(boardIdx);
	}

	@Override
	public BoardFileDto selectBoardFileInformation(int idx, int boardIdx) throws Exception {
		return boardMapper.selectBoardFileInformation(idx,boardIdx);
	}
	
	
	
}
