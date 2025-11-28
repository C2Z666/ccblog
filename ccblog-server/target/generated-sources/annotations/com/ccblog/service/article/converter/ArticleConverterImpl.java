package com.ccblog.service.article.converter;

import com.ccblog.dto.article.ArticleDTO;
import com.ccblog.enumeration.article.SourceTypeEnum;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2025-11-28T09:42:34+0800",
    comments = "version: 1.5.3.Final, compiler: javac, environment: Java 17.0.12 (Oracle Corporation)"
)
@Component
public class ArticleConverterImpl implements ArticleConverter {

    @Override
    public ArticleDTO entityToDTO(ArticleDTO article) {
        if ( article == null ) {
            return null;
        }

        ArticleDTO articleDTO = new ArticleDTO();

        articleDTO.setArticleId( article.getArticleId() );
        articleDTO.setArticleType( article.getArticleType() );
        articleDTO.setAuthor( article.getAuthor() );
        articleDTO.setAuthorName( article.getAuthorName() );
        articleDTO.setAuthorAvatar( article.getAuthorAvatar() );
        articleDTO.setVersion( article.getVersion() );
        articleDTO.setTitle( article.getTitle() );
        articleDTO.setShortTitle( article.getShortTitle() );
        articleDTO.setSummary( article.getSummary() );
        articleDTO.setCover( article.getCover() );
        articleDTO.setContent( article.getContent() );
        articleDTO.setSourceType( article.getSourceType() );
        articleDTO.setSourceUrl( article.getSourceUrl() );
        articleDTO.setStatus( article.getStatus() );
        articleDTO.setOfficalStat( article.getOfficalStat() );
        articleDTO.setToppingStat( article.getToppingStat() );
        articleDTO.setCreamStat( article.getCreamStat() );
        articleDTO.setCreateTime( article.getCreateTime() );
        articleDTO.setLastUpdateTime( article.getLastUpdateTime() );
        articleDTO.setCategoryId( article.getCategoryId() );
        articleDTO.setLiked( article.getLiked() );
        articleDTO.setCommented( article.getCommented() );
        articleDTO.setCollected( article.getCollected() );
        articleDTO.setCount( article.getCount() );

        return articleDTO;
    }
}
